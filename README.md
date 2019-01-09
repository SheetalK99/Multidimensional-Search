# Multidimensional-Search

Multidimensional search implementation that allows efficient search by multiple attributes by creating references to objects for each search field. This is useful for several applications like ecommerce websites that have millions of products each with multiple attributes like Name, Brand, Price, Size, Description, etc. The search feature should allow searching by any of these attributes. Hence, a specilaised data structure is required that supports multidimensional search and is consistent at all times.

## Supported Operations
* Insert: Insert a new item. If it already exists replace the item with new values for all attributes
* Find   :Find item by id
* Delete : Delete item by id
* FindMinPrice : Find item with certain description that has least price
* FindMaxPrice : Find item with certain description that has maximum price
* FindPriceRange: Find items in given price range
* PriceHike: Increase price of product by x%
* RemoveNames: Remove items with certain description

## Getting started
Give as input test data to the Driver file
