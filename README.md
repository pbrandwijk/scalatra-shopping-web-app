# Scalatra Shopping Web App #

## Build & Run ##

```sh
$ cd scalatra-shopping-web-app
$ sbt
> jetty:start
> browse
```

If `browse` doesn't launch your browser, manually open [http://localhost:8080/](http://localhost:8080/) in your browser.

## Run the tests ##

```sh
$ cd scalatra-shopping-web-app
$ sbt
> test
```

## Add a product ##

Here [httpie](https://httpie.org/) is used to call the REST API over the command line. Feel free to adapt these commands to your favorite
tool. The `echo` command is used to pipe the JSON through to `http`. This keeps the JSON intact and allows for other
values than strings to be passed.

```sh
echo '{ "id": "book001", "description": "The Da Vinci Code", "price": 9.95, "stock": 10 }' | http --verbose POST http://localhost:8080/products/addProduct
```

If the call was successful, you get the product back in JSON format. If the JSON in your request was invalid or incomplete, or
the product could not be added, you get an error message in the response header field `ACK`.

If you perform this call more than once for the same product id, the product description, price and stock will be overwritten.

## Get all products as JSON ##

```sh
http --verbose GET http://localhost:8080/products/
```

## Add a user ##

```sh
echo '{ "email": "johndoe@example.com", "name": "John Doe", "bankAccount": "9876543210" }' | http --verbose POST http://localhost:8080/users/addUser
```

If the call was successful, you get the user back in JSON format. If the JSON in your request was invalid or incomplete, or
the user could not be added, you get an error message in the response header field `ACK`.

If you perform this call more than once for the same email address, then no change will be made.

## Get all users as JSON ##

```sh
http --verbose GET http://localhost:8080/users/
```

## Add an item to a user's shopping chart ##

If either the given user or the given product does not exist, this call has no action. Also, if the requested quantity
exceeds the available product stock, there is also no action.

```sh
echo '{ "email": "johndoe@example.com", "id": "book001", "quantity": "1" }' | http --verbose POST http://localhost:8080/users/addItemToUserChart
```

If the call was successful, you get the user back in JSON format. If the JSON in your request was invalid or incomplete, or
the item could not be added to the chart, you get an error message in the response header field `ACK`.

## Checkout a user's shopping chart ##

```sh
echo '{ "email": "johndoe@example.com", "address": "Mainstreet 1, Johnsville" }' | http --verbose POST http://localhost:8080/users/checkout
```

If the call was successful, you get the order number and total price of the order back in JSON format. If the JSON in your 
request was invalid or incomplete, or the order could not be added, you get an error message in the response header field `ACK`.

## Get all orders as JSON ##

```sh
http --verbose GET http://localhost:8080/users/orders
```