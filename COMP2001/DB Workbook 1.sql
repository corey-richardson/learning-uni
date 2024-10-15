IF OBJECT_ID(N'dbo.Orders', N'U') IS NOT NULL
BEGIN
	DROP TABLE dbo.Orders;
END
CREATE TABLE dbo.Orders
(
    OrderId INT NOT NULL,
    OrderDate Date NOT NULL,
    Customer NVARCHAR(5) NOT NULL,

    CONSTRAINT pk_Orders PRIMARY KEY (OrderID)
);

INSERT INTO dbo.Orders (OrderId, OrderDate, Customer)
VALUES (1, '2019-06-22', '1');
INSERT INTO dbo.Orders (OrderId, OrderDate, Customer)
VALUES (2, '2019-07-22', '2');
INSERT INTO dbo.Orders (OrderId, OrderDate, Customer)
VALUES (3, '2019-08-22', '3');


IF OBJECT_ID(N'dbo.OrderDetails', N'U') IS NOT NULL
BEGIN
	DROP TABLE dbo.OrderDetails;
END

CREATE TABLE dbo.OrderDetails
(
    OrderId INT NOT NULL,
    ProductId INT NOT NULL,
    Quantity INT NOT NULL
        CHECK(Quantity <> 0),
CONSTRAINT PK_OrderDetails PRIMARY KEY (OrderId, ProductId)
);

INSERT INTO dbo.OrderDetails(OrderId, ProductId, Quantity)
VALUES(1, 1, 20);
INSERT INTO dbo.OrderDetails(OrderId, ProductId, Quantity)
VALUES(2, 2, 10);
INSERT INTO dbo.OrderDetails(OrderId, ProductId, Quantity)
VALUES(3, 3, 5);

IF OBJECT_ID(N'dbo.Products', N'U') IS NOT NULL
BEGIN
	DROP TABLE dbo.Products;
END

CREATE TABLE dbo.Products
(
	ProductId INT IDENTITY(1,1) not null,
	Product_Details VARCHAR (Max) not null,
	Price FLOAT not null,
	Quantity int not null,
	CONSTRAINT PK_Products PRIMARY KEY (ProductId)
);

INSERT INTO dbo.Products(Product_Details, Price, Quantity)
VALUES('Pens - Black biro',0.30, 200);
INSERT INTO dbo.Products(Product_Details, Price, Quantity)
VALUES('Pens - Green biro',0.30, 200);
INSERT INTO dbo.Products(Product_Details, Price, Quantity)
VALUES('Pens - Red biro',0.30, 200);
