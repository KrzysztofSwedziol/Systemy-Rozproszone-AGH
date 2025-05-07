#!/usr/bin/env python3
import sys
import pathlib

sys.path.append(str(pathlib.Path(__file__).parent / "idl"))

import Ice
from idl.ServantsManagement import LoggerPrx, SessionPrx

def main(argv):
    with Ice.initialize(argv) as communicator:
        logger = LoggerPrx.checkedCast(
            communicator.stringToProxy("Logger/logger:default -p 10000")
        )
        if not logger:
            raise RuntimeError("Couldn't create logger")

        logger.logMessage("Python client started")

        user_id = input("Submit user identifier: ").strip() or "guest"

        session = SessionPrx.checkedCast(
            communicator.stringToProxy(f"Session/{user_id}:default -p 10000")
        )
        if not session:
            raise RuntimeError("Couldn't create proxy for session")

        count = session.performAction()
        info  = session.getSessionInfo()

        print(f"[CLIENT] performAction() returned: {count}")
        print(f"[CLIENT] getSessionInfo() => {info}")

        logger.logMessage(f"User {user_id} performed action nr {count}")

if __name__ == "__main__":
    sys.exit(main(sys.argv))
