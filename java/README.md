# Gumshoe Java
Java implementation of Gumshoe.  Design and usage is consistent with the
[cross-language readme](../README.md).

## Usage
Bring Gumshoe into your project as a maven dependency:

```
to be written after release
```

Somewhere duing your application's initialization, you need to configure
Gumshoe:

```java
Configuration config = new SimpleConfiguration("my-app");
// specify a Publisher, Filter and Decorator or use defaults
Gumshoe.configure(config);
```

Within the business logic of your code, you need to:

1.  Establish Gumshoe context(s)
2.  Store data in the Gumshoe context.
3.  Emit events using Gumshoe.
4.  Profit!

An example:

```java
Gumshoe gumshoe = Gumshoe.get();
gumshoe.context("data_export").start();

File csv = getCsvFile();

for (String customerName : getCustomerNames() {
    gumshoe.context("processing_customer")
           .put("customer", "customerName")
           .start();
    try {
        for (Item item: getItemsOf(customerName) {
            gumshoe.context("processing_item")
                   .put("item", item.getId())
                   .start();

            try {
                writeItemLine(csv, product);
                gumshoe.context().finish();
            } catch (Exception e) {
                gumshoe.context().fail();
            }
        }
        gumshoe.context().finish();
    } catch (Exception e) {
        gumshoe.context().fail();
    }
}
gumshoe.finish();
```

This will result in a stream of JSON events sharing the same stream ID.  For
each context started, there would be a started event, and then a subsequent
finished event when they are finished.  The context of the events would be
data export, data export / processing customer or data export / processing
customer / processing item depending on where in the code the event is being
emitted from.  The customer name and item id would be included in the events
as appropriate.  As each item and customer were done processing, a finished
event would be emitted for that context.  If exceptions were caught when
processing an item or a customer, a failed event would be emitted for those
respective contexts.

These events would be published according to the Publisher that Gumshoe was
configured with.  You could then observe/search through these events as
allowed in whatever tools you use to consume them.
