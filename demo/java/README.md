# Gumshoe Demo Application
This application generates some random vehicle sales data, then aggregates it
by manufacturer, type and model and displays its output.  It has been sprinkled
with Gumshoe goodness to allow event outputting to the gumshoe.log file.  To
see this in action, do the following:

1. Open a terminal window and type ```tail -f gumshoe.log | jq "."```
2. Run the application with ```VEHICLES=[some number] mvn exec:java```

For example, running the demo application with ```VEHICLES=2``` results in
the following events being emitted to the log:

```
./java $ cat gumshoe.log | jq .

{
  "_pid": "19733",
  "_hostname": "lwoodson-ret.local",
  "stream_id": "32e9ca27-0f5a-4f65-bccb-fbb51082169d",
  "context": [
    "session"
  ],
  "_thread": "com.bazaarvoice.gumshoe.Application.main()",
  "_user": "lance.woodson",
  "type": "started"
}
{
  "_pid": "19733",
  "_hostname": "lwoodson-ret.local",
  "stream_id": "32e9ca27-0f5a-4f65-bccb-fbb51082169d",
  "context": [
    "session",
    "generating random vehicles"
  ],
  "count": 2,
  "_thread": "com.bazaarvoice.gumshoe.Application.main()",
  "_user": "lance.woodson",
  "type": "started"
}
{
  "_pid": "19733",
  "_hostname": "lwoodson-ret.local",
  "stream_id": "32e9ca27-0f5a-4f65-bccb-fbb51082169d",
  "context": [
    "session",
    "generating random vehicles"
  ],
  "count": 2,
  "_thread": "com.bazaarvoice.gumshoe.Application.main()",
  "_user": "lance.woodson",
  "type": "finished",
  "execution_time": 3
}
{
  "_pid": "19733",
  "_hostname": "lwoodson-ret.local",
  "stream_id": "32e9ca27-0f5a-4f65-bccb-fbb51082169d",
  "context": [
    "session",
    "initializing model aggregations"
  ],
  "_thread": "com.bazaarvoice.gumshoe.Application.main()",
  "_user": "lance.woodson",
  "type": "started"
}
{
  "_pid": "19733",
  "_hostname": "lwoodson-ret.local",
  "stream_id": "32e9ca27-0f5a-4f65-bccb-fbb51082169d",
  "context": [
    "session",
    "initializing model aggregations"
  ],
  "_thread": "com.bazaarvoice.gumshoe.Application.main()",
  "_user": "lance.woodson",
  "type": "finished",
  "execution_time": 2
}
{
  "_pid": "19733",
  "_hostname": "lwoodson-ret.local",
  "stream_id": "32e9ca27-0f5a-4f65-bccb-fbb51082169d",
  "context": [
    "session",
    "initializing type aggregations"
  ],
  "_thread": "com.bazaarvoice.gumshoe.Application.main()",
  "_user": "lance.woodson",
  "type": "started"
}
{
  "_pid": "19733",
  "_hostname": "lwoodson-ret.local",
  "stream_id": "32e9ca27-0f5a-4f65-bccb-fbb51082169d",
  "context": [
    "session",
    "initializing type aggregations"
  ],
  "_thread": "com.bazaarvoice.gumshoe.Application.main()",
  "_user": "lance.woodson",
  "type": "finished",
  "execution_time": 1
}
{
  "_pid": "19733",
  "_hostname": "lwoodson-ret.local",
  "stream_id": "32e9ca27-0f5a-4f65-bccb-fbb51082169d",
  "context": [
    "session",
    "initializing model aggregations"
  ],
  "_thread": "com.bazaarvoice.gumshoe.Application.main()",
  "_user": "lance.woodson",
  "type": "started"
}
{
  "_pid": "19733",
  "_hostname": "lwoodson-ret.local",
  "stream_id": "32e9ca27-0f5a-4f65-bccb-fbb51082169d",
  "context": [
    "session",
    "initializing model aggregations"
  ],
  "_thread": "com.bazaarvoice.gumshoe.Application.main()",
  "_user": "lance.woodson",
  "type": "finished",
  "execution_time": 1
}
{
  "_pid": "19733",
  "_hostname": "lwoodson-ret.local",
  "stream_id": "32e9ca27-0f5a-4f65-bccb-fbb51082169d",
  "context": [
    "session",
    "processing vehicles"
  ],
  "_thread": "com.bazaarvoice.gumshoe.Application.main()",
  "_user": "lance.woodson",
  "type": "started"
}
{
  "_pid": "19733",
  "_hostname": "lwoodson-ret.local",
  "stream_id": "32e9ca27-0f5a-4f65-bccb-fbb51082169d",
  "context": [
    "session",
    "processing vehicles",
    "collecting vehicle data"
  ],
  "_thread": "com.bazaarvoice.gumshoe.Application.main()",
  "vehicle_type": "SEDAN",
  "model": "ACCORD",
  "_user": "lance.woodson",
  "type": "started",
  "manufacturer": "HONDA"
}
{
  "_pid": "19733",
  "_hostname": "lwoodson-ret.local",
  "stream_id": "32e9ca27-0f5a-4f65-bccb-fbb51082169d",
  "context": [
    "session",
    "processing vehicles",
    "collecting vehicle data"
  ],
  "_thread": "com.bazaarvoice.gumshoe.Application.main()",
  "vehicle_type": "SEDAN",
  "model": "ACCORD",
  "_user": "lance.woodson",
  "type": "finished",
  "manufacturer": "HONDA",
  "execution_time": 1
}
{
  "_pid": "19733",
  "_hostname": "lwoodson-ret.local",
  "stream_id": "32e9ca27-0f5a-4f65-bccb-fbb51082169d",
  "context": [
    "session",
    "processing vehicles",
    "collecting vehicle data"
  ],
  "_thread": "com.bazaarvoice.gumshoe.Application.main()",
  "vehicle_type": "SEDAN",
  "model": "FUSION",
  "_user": "lance.woodson",
  "type": "started",
  "manufacturer": "FORD"
}
{
  "_pid": "19733",
  "_hostname": "lwoodson-ret.local",
  "stream_id": "32e9ca27-0f5a-4f65-bccb-fbb51082169d",
  "context": [
    "session",
    "processing vehicles",
    "collecting vehicle data"
  ],
  "_thread": "com.bazaarvoice.gumshoe.Application.main()",
  "vehicle_type": "SEDAN",
  "model": "FUSION",
  "_user": "lance.woodson",
  "type": "finished",
  "manufacturer": "FORD",
  "execution_time": 1
}
{
  "_pid": "19733",
  "_hostname": "lwoodson-ret.local",
  "stream_id": "32e9ca27-0f5a-4f65-bccb-fbb51082169d",
  "context": [
    "session",
    "processing vehicles"
  ],
  "_thread": "com.bazaarvoice.gumshoe.Application.main()",
  "_user": "lance.woodson",
  "type": "finished",
  "execution_time": 2
}
{
  "_pid": "19733",
  "_hostname": "lwoodson-ret.local",
  "stream_id": "32e9ca27-0f5a-4f65-bccb-fbb51082169d",
  "context": [
    "session",
    "aggregating aggregations"
  ],
  "_thread": "com.bazaarvoice.gumshoe.Application.main()",
  "_user": "lance.woodson",
  "type": "started"
}
{
  "_pid": "19733",
  "_hostname": "lwoodson-ret.local",
  "stream_id": "32e9ca27-0f5a-4f65-bccb-fbb51082169d",
  "context": [
    "session",
    "aggregating aggregations"
  ],
  "_thread": "com.bazaarvoice.gumshoe.Application.main()",
  "_user": "lance.woodson",
  "type": "finished",
  "execution_time": 0
}
{
  "_pid": "19733",
  "_hostname": "lwoodson-ret.local",
  "stream_id": "32e9ca27-0f5a-4f65-bccb-fbb51082169d",
  "context": [
    "session",
    "output"
  ],
  "_thread": "com.bazaarvoice.gumshoe.Application.main()",
  "_user": "lance.woodson",
  "type": "started"
}
{
  "_pid": "19733",
  "_hostname": "lwoodson-ret.local",
  "stream_id": "32e9ca27-0f5a-4f65-bccb-fbb51082169d",
  "context": [
    "session",
    "output"
  ],
  "_thread": "com.bazaarvoice.gumshoe.Application.main()",
  "_user": "lance.woodson",
  "type": "finished",
  "execution_time": 7
}
```
