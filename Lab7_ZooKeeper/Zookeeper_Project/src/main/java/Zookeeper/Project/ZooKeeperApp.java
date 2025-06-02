package Zookeeper.Project;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ZooKeeperApp implements Watcher {
    private static final String ZOOKEEPER_ADDRESS = "localhost:2181";
    private static final int SESSION_TIMEOUT = 3000;
    private static final String TARGET_ZNODE = "/a";

    private ZooKeeper zooKeeper;
    private TreeGUI treeGUI;
    private final CountDownLatch connectedLatch = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        ZooKeeperApp app = new ZooKeeperApp();
        app.connectToZookeeper();
        app.run();
        app.close();
    }

    public void connectToZookeeper() throws IOException, InterruptedException {
        this.zooKeeper = new ZooKeeper(ZOOKEEPER_ADDRESS, SESSION_TIMEOUT, this);
        connectedLatch.await();
    }

    public void run() throws InterruptedException {
        watchTargetZnode();
        synchronized (this) {
            wait();
        }
    }

    public void watchTargetZnode() throws InterruptedException {
        try {
            Stat stat = zooKeeper.exists(TARGET_ZNODE, this);
            if (stat != null) {
                startTreeGUI();
                watchChildrenRecursive(TARGET_ZNODE);
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    public void watchChildrenRecursive(String path) throws InterruptedException, KeeperException {
        try {
            zooKeeper.getChildren(path, this);
            List<String> children = zooKeeper.getChildren(path, false);
            for (String child : children) {
                String childPath = path + "/" + child;
                watchChildrenRecursive(childPath);
            }
        } catch (KeeperException.NoNodeException e) {
        }
    }

    public void startTreeGUI() {
        if (treeGUI == null) {
            treeGUI = new TreeGUI();
            SwingUtilities.invokeLater(() -> {
                treeGUI.setVisible(true);
                updateTreeGUI();
            });
        }
    }

    public void closeTreeGUI() {
        if (treeGUI != null) {
            SwingUtilities.invokeLater(() -> {
                treeGUI.dispose();
                treeGUI = null;
            });
        }
    }

    public void close() throws InterruptedException {
        zooKeeper.close();
        closeTreeGUI();
    }

    private void updateTreeGUI() {
        if (treeGUI == null) return;

        try {
            DefaultMutableTreeNode rootNode = buildTreeNode(TARGET_ZNODE);
            SwingUtilities.invokeLater(() -> {
                if (treeGUI != null) {
                    treeGUI.updateTree(new DefaultTreeModel(rootNode));
                }
            });
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private DefaultMutableTreeNode buildTreeNode(String path) throws KeeperException, InterruptedException {
        String nodeName = path.equals(TARGET_ZNODE) ? path : path.substring(path.lastIndexOf('/') + 1);
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(nodeName);

        try {
            List<String> children = zooKeeper.getChildren(path, false);
            for (String child : children) {
                String childPath = path + "/" + child;
                node.add(buildTreeNode(childPath));
            }
        } catch (KeeperException.NoNodeException e) {
        }
        return node;
    }

    @Override
    public void process(WatchedEvent event) {
        switch (event.getType()) {
            case None:
                if (event.getState() == Event.KeeperState.SyncConnected) {
                    connectedLatch.countDown();
                }
                break;
            case NodeCreated:
                if (event.getPath().equals(TARGET_ZNODE)) {
                    try {
                        startTreeGUI();
                        watchTargetZnode();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    updateTreeGUI();
                }
                break;
            case NodeDeleted:
                if (event.getPath().equals(TARGET_ZNODE)) {
                    closeTreeGUI();
                } else {
                    updateTreeGUI();
                }
                break;
            case NodeChildrenChanged:
                try {
                    watchChildrenRecursive(event.getPath());
                    updateTreeGUI();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    static class TreeGUI extends JFrame {
        private JTree tree;
        private DefaultTreeModel treeModel;

        public TreeGUI() {
            setTitle("ZooKeeper Node Tree: " + TARGET_ZNODE);
            setSize(500, 700);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new BorderLayout());

            treeModel = new DefaultTreeModel(new DefaultMutableTreeNode("Loading..."));
            tree = new JTree(treeModel);
            tree.setShowsRootHandles(true);
            tree.setRootVisible(true);

            JScrollPane scrollPane = new JScrollPane(tree);
            add(scrollPane, BorderLayout.CENTER);
        }

        public void updateTree(TreeModel model) {
            treeModel = (DefaultTreeModel) model;
            tree.setModel(treeModel);
            expandAllNodes();
        }

        private void expandAllNodes() {
            for (int i = 0; i < tree.getRowCount(); i++) {
                tree.expandRow(i);
            }
        }
    }
}