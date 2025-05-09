# Generated by the gRPC Python protocol compiler plugin. DO NOT EDIT!
"""Client and server classes corresponding to protobuf-defined services."""
import grpc
import warnings

from google.protobuf import empty_pb2 as google_dot_protobuf_dot_empty__pb2
from smart.COSensor import co_sensor_pb2 as smart_dot_COSensor_dot_co__sensor__pb2

GRPC_GENERATED_VERSION = '1.71.0'
GRPC_VERSION = grpc.__version__
_version_not_supported = False

try:
    from grpc._utilities import first_version_is_lower
    _version_not_supported = first_version_is_lower(GRPC_VERSION, GRPC_GENERATED_VERSION)
except ImportError:
    _version_not_supported = True

if _version_not_supported:
    raise RuntimeError(
        f'The grpc package installed is at version {GRPC_VERSION},'
        + f' but the generated code in smart/COSensor/co_sensor_pb2_grpc.py depends on'
        + f' grpcio>={GRPC_GENERATED_VERSION}.'
        + f' Please upgrade your grpc module to grpcio>={GRPC_GENERATED_VERSION}'
        + f' or downgrade your generated code using grpcio-tools<={GRPC_VERSION}.'
    )


class COSensorStub(object):
    """Missing associated documentation comment in .proto file."""

    def __init__(self, channel):
        """Constructor.

        Args:
            channel: A grpc.Channel.
        """
        self.GetState = channel.unary_unary(
                '/smart.co.COSensor/GetState',
                request_serializer=google_dot_protobuf_dot_empty__pb2.Empty.SerializeToString,
                response_deserializer=smart_dot_COSensor_dot_co__sensor__pb2.SensorState.FromString,
                _registered_method=True)
        self.ToggleSensor = channel.unary_unary(
                '/smart.co.COSensor/ToggleSensor',
                request_serializer=google_dot_protobuf_dot_empty__pb2.Empty.SerializeToString,
                response_deserializer=smart_dot_COSensor_dot_co__sensor__pb2.Ack.FromString,
                _registered_method=True)
        self.ReadCOLevel = channel.unary_unary(
                '/smart.co.COSensor/ReadCOLevel',
                request_serializer=google_dot_protobuf_dot_empty__pb2.Empty.SerializeToString,
                response_deserializer=smart_dot_COSensor_dot_co__sensor__pb2.SensorState.FromString,
                _registered_method=True)


class COSensorServicer(object):
    """Missing associated documentation comment in .proto file."""

    def GetState(self, request, context):
        """Missing associated documentation comment in .proto file."""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')

    def ToggleSensor(self, request, context):
        """Missing associated documentation comment in .proto file."""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')

    def ReadCOLevel(self, request, context):
        """Missing associated documentation comment in .proto file."""
        context.set_code(grpc.StatusCode.UNIMPLEMENTED)
        context.set_details('Method not implemented!')
        raise NotImplementedError('Method not implemented!')


def add_COSensorServicer_to_server(servicer, server):
    rpc_method_handlers = {
            'GetState': grpc.unary_unary_rpc_method_handler(
                    servicer.GetState,
                    request_deserializer=google_dot_protobuf_dot_empty__pb2.Empty.FromString,
                    response_serializer=smart_dot_COSensor_dot_co__sensor__pb2.SensorState.SerializeToString,
            ),
            'ToggleSensor': grpc.unary_unary_rpc_method_handler(
                    servicer.ToggleSensor,
                    request_deserializer=google_dot_protobuf_dot_empty__pb2.Empty.FromString,
                    response_serializer=smart_dot_COSensor_dot_co__sensor__pb2.Ack.SerializeToString,
            ),
            'ReadCOLevel': grpc.unary_unary_rpc_method_handler(
                    servicer.ReadCOLevel,
                    request_deserializer=google_dot_protobuf_dot_empty__pb2.Empty.FromString,
                    response_serializer=smart_dot_COSensor_dot_co__sensor__pb2.SensorState.SerializeToString,
            ),
    }
    generic_handler = grpc.method_handlers_generic_handler(
            'smart.co.COSensor', rpc_method_handlers)
    server.add_generic_rpc_handlers((generic_handler,))
    server.add_registered_method_handlers('smart.co.COSensor', rpc_method_handlers)


 # This class is part of an EXPERIMENTAL API.
class COSensor(object):
    """Missing associated documentation comment in .proto file."""

    @staticmethod
    def GetState(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_unary(
            request,
            target,
            '/smart.co.COSensor/GetState',
            google_dot_protobuf_dot_empty__pb2.Empty.SerializeToString,
            smart_dot_COSensor_dot_co__sensor__pb2.SensorState.FromString,
            options,
            channel_credentials,
            insecure,
            call_credentials,
            compression,
            wait_for_ready,
            timeout,
            metadata,
            _registered_method=True)

    @staticmethod
    def ToggleSensor(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_unary(
            request,
            target,
            '/smart.co.COSensor/ToggleSensor',
            google_dot_protobuf_dot_empty__pb2.Empty.SerializeToString,
            smart_dot_COSensor_dot_co__sensor__pb2.Ack.FromString,
            options,
            channel_credentials,
            insecure,
            call_credentials,
            compression,
            wait_for_ready,
            timeout,
            metadata,
            _registered_method=True)

    @staticmethod
    def ReadCOLevel(request,
            target,
            options=(),
            channel_credentials=None,
            call_credentials=None,
            insecure=False,
            compression=None,
            wait_for_ready=None,
            timeout=None,
            metadata=None):
        return grpc.experimental.unary_unary(
            request,
            target,
            '/smart.co.COSensor/ReadCOLevel',
            google_dot_protobuf_dot_empty__pb2.Empty.SerializeToString,
            smart_dot_COSensor_dot_co__sensor__pb2.SensorState.FromString,
            options,
            channel_credentials,
            insecure,
            call_credentials,
            compression,
            wait_for_ready,
            timeout,
            metadata,
            _registered_method=True)
