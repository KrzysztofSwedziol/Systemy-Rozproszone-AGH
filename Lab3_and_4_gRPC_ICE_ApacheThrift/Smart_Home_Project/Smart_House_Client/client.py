import grpc
from smart.fridge import fridge_pb2, fridge_pb2_grpc
from smart.camera import camera_pb2, camera_pb2_grpc
from smart.COSensor import co_sensor_pb2, co_sensor_pb2_grpc
def print_fridge_commands():
    print("""
Available fridge commands:
  status             - Show full fridge state
  get_temp_fridge    - Get fridge temperature
  get_temp_freezer   - Get freezer temperature
  set_temp_fridge    - Set fridge temperature
  set_temp_freezer   - Set freezer temperature
  get_light          - Check if light is on
  toggle_light       - Toggle fridge light
  get_contents       - Show fridge and freezer contents
  add_product        - Add product to fridge/freezer
  remove_product     - Remove product from fridge/freezer
  back               - Return to main menu
""")

def fridge_mode(main_stub):
    print_fridge_commands()
    while True:
        cmd = input("Fridge> ").strip().lower()

        if cmd == "status":
            response = main_stub.GetState(fridge_pb2.google_dot_protobuf_dot_empty__pb2.Empty())
            print(response)

        elif cmd == "get_temp_fridge":
            resp = main_stub.GetFridgeTemperature(fridge_pb2.google_dot_protobuf_dot_empty__pb2.Empty())
            print(f"Fridge temperature: {resp.temperature} °C")

        elif cmd == "get_temp_freezer":
            resp = main_stub.GetFreezerTemperature(fridge_pb2.google_dot_protobuf_dot_empty__pb2.Empty())
            print(f"Freezer temperature: {resp.temperature} °C")

        elif cmd == "set_temp_fridge":
            temp = float(input("Enter new fridge temperature: "))
            ack = main_stub.SetFridgeTemperature(fridge_pb2.TemperatureRequest(targetTemperature=temp))
            print(ack.message)

        elif cmd == "set_temp_freezer":
            temp = float(input("Enter new freezer temperature: "))
            ack = main_stub.SetFreezerTemperature(fridge_pb2.TemperatureRequest(targetTemperature=temp))
            print(ack.message)

        elif cmd == "get_light":
            resp = main_stub.GetLight(fridge_pb2.google_dot_protobuf_dot_empty__pb2.Empty())
            status = "ON" if resp.lightOn else "OFF"
            print(f"Fridge light is {status}")

        elif cmd == "toggle_light":
            ack = main_stub.ToggleLight(fridge_pb2.google_dot_protobuf_dot_empty__pb2.Empty())
            print(ack.message)

        elif cmd == "get_contents":
            contents = main_stub.GetContents(fridge_pb2.google_dot_protobuf_dot_empty__pb2.Empty())
            print("Fridge contents:")
            for item in contents.fridgeContents:
                print(f"  {item.name}: {item.quantity}")
            print("Freezer contents:")
            for item in contents.freezerContents:
                print(f"  {item.name}: {item.quantity}")

        elif cmd == "add_product":
            zone = input("Where? (fridge/freezer): ").strip().lower()
            name = input("Product name: ")
            qty = int(input("Quantity: "))
            item = fridge_pb2.Item(name=name, quantity=qty)
            if zone == "fridge":
                ack = main_stub.AddProductToFridge(item)
            else:
                ack = main_stub.AddProductToFreezer(item)
            print(ack.message)

        elif cmd == "remove_product":
            zone = input("Where? (fridge/freezer): ").strip().lower()
            name = input("Product name: ")
            qty = int(input("Quantity to remove: "))
            item = fridge_pb2.Item(name=name, quantity=qty)
            if zone == "fridge":
                ack = main_stub.RemoveProductFromFridge(item)
            else:
                ack = main_stub.RemoveProductFromFreezer(item)
            print(ack.message)

        elif cmd == "back":
            break

        else:
            print("Unknown command.")
            print_fridge_commands()

def print_camera_commands():
    print("""
Available camera commands:
  list           - List available cameras
  state          - Get camera state
  power_on       - Turn camera ON
  power_off      - Turn camera OFF
  move           - Move PTZ camera (if supported)
  back           - Return to main menu
""")


def camera_mode(camera_stub):
    camera_id = input("Enter camera ID (cam1 or cam2): ").strip()
    print_camera_commands()

    while True:
        cmd = input(f"Camera[{camera_id}]> ").strip().lower()

        if cmd == "list":
            resp = camera_stub.ListCameras(camera_pb2.google_dot_protobuf_dot_empty__pb2.Empty())
            print("Available Cameras:")
            for cam in resp.cameras:
                print(f"ID: {cam.camera_id}, Type: {cam.type}, IP: {cam.ip}")

        elif cmd == "state":
            req = camera_pb2.CameraRequest(camera_id=camera_id)
            state = camera_stub.GetState(req)
            print(state)

        elif cmd == "power_on":
            ack = camera_stub.TogglePower(camera_pb2.TogglePowerRequest(camera_id=camera_id, turnOn=True))
            print(ack.message)

        elif cmd == "power_off":
            ack = camera_stub.TogglePower(camera_pb2.TogglePowerRequest(camera_id=camera_id, turnOn=False))
            print(ack.message)

        elif cmd == "move":
            angle = int(input("Enter angle: "))
            tilt = int(input("Enter tilt: "))
            try:
                ack = camera_stub.MoveCamera(camera_pb2.MoveCameraRequest(camera_id=camera_id, angle=angle, tilt=tilt))
                print(ack.message)
            except grpc.RpcError as e:
                print(f"Error: {e.details()}")

        elif cmd == "back":
            break

        else:
            print("Unknown command.")
            print_camera_commands()

def print_co_commands():
    print("""
Available CO Sensor commands:
  state       - Get sensor state
  toggle      - Turn sensor ON/OFF
  read        - Read current CO level
  back        - Return to main menu
""")

def co_sensor_mode(co_stub):
    print_co_commands()
    while True:
        cmd = input("CO Sensor> ").strip().lower()

        if cmd == "state":
            state = co_stub.GetState(co_sensor_pb2.google_dot_protobuf_dot_empty__pb2.Empty())
            status = "ON" if state.is_on else "OFF"
            print(f"Sensor is {status}, CO Level: {state.co_level:.2f} ppm")

        elif cmd == "toggle":
            ack = co_stub.ToggleSensor(co_sensor_pb2.google_dot_protobuf_dot_empty__pb2.Empty())
            print(ack.message)

        elif cmd == "read":
            state = co_stub.ReadCOLevel(co_sensor_pb2.google_dot_protobuf_dot_empty__pb2.Empty())
            if state.is_on:
                print(f"Current CO Level: {state.co_level:.2f} ppm")
            else:
                print("Sensor is OFF. Turn it on to read CO level.")

        elif cmd == "back":
            break

        else:
            print("Unknown command.")
            print_co_commands()

def main():
    main_channel = grpc.insecure_channel('localhost:50051')
    main_stub = fridge_pb2_grpc.FridgeStub(main_channel)
    co_stub = co_sensor_pb2_grpc.COSensorStub(main_channel)

    camera_channel = grpc.insecure_channel('localhost:50052')
    camera_stub = camera_pb2_grpc.CameraServiceStub(camera_channel)

    print("Welcome to Smart House Client!")
    while True:
        choice = input("Select device (fridge / camera / co_sensor / exit): ").strip().lower()
        if choice == "fridge":
            fridge_mode(main_stub)
        elif choice == "camera":
            camera_mode(camera_stub)
        elif choice == "co_sensor":
            co_sensor_mode(co_stub)
        elif choice == "exit":
            print("Goodbye!")
            break
        else:
            print("Unknown device. Try again.")

if __name__ == '__main__':
    main()
