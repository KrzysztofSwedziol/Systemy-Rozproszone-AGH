# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# NO CHECKED-IN PROTOBUF GENCODE
# source: smart/COSensor/co_sensor.proto
# Protobuf Python Version: 5.29.0
"""Generated protocol buffer code."""
from google.protobuf import descriptor as _descriptor
from google.protobuf import descriptor_pool as _descriptor_pool
from google.protobuf import runtime_version as _runtime_version
from google.protobuf import symbol_database as _symbol_database
from google.protobuf.internal import builder as _builder
_runtime_version.ValidateProtobufRuntimeVersion(
    _runtime_version.Domain.PUBLIC,
    5,
    29,
    0,
    '',
    'smart/COSensor/co_sensor.proto'
)
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()


from google.protobuf import empty_pb2 as google_dot_protobuf_dot_empty__pb2


DESCRIPTOR = _descriptor_pool.Default().AddSerializedFile(b'\n\x1esmart/COSensor/co_sensor.proto\x12\x08smart.co\x1a\x1bgoogle/protobuf/empty.proto\".\n\x0bSensorState\x12\r\n\x05is_on\x18\x01 \x01(\x08\x12\x10\n\x08\x63o_level\x18\x02 \x01(\x02\"\x16\n\x03\x41\x63k\x12\x0f\n\x07message\x18\x01 \x01(\t2\xba\x01\n\x08\x43OSensor\x12\x39\n\x08GetState\x12\x16.google.protobuf.Empty\x1a\x15.smart.co.SensorState\x12\x35\n\x0cToggleSensor\x12\x16.google.protobuf.Empty\x1a\r.smart.co.Ack\x12<\n\x0bReadCOLevel\x12\x16.google.protobuf.Empty\x1a\x15.smart.co.SensorStateb\x06proto3')

_globals = globals()
_builder.BuildMessageAndEnumDescriptors(DESCRIPTOR, _globals)
_builder.BuildTopDescriptorsAndMessages(DESCRIPTOR, 'smart.COSensor.co_sensor_pb2', _globals)
if not _descriptor._USE_C_DESCRIPTORS:
  DESCRIPTOR._loaded_options = None
  _globals['_SENSORSTATE']._serialized_start=73
  _globals['_SENSORSTATE']._serialized_end=119
  _globals['_ACK']._serialized_start=121
  _globals['_ACK']._serialized_end=143
  _globals['_COSENSOR']._serialized_start=146
  _globals['_COSENSOR']._serialized_end=332
# @@protoc_insertion_point(module_scope)
