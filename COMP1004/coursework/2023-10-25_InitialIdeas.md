# Initial Ideas

## General Ideas

### Food Expiration Date Tracker
- As a user I want to be able to create `Food` objects to track expiry-date related characteristics of food items. These could include: 
    - `string` Food item name
    - `datetime` Date of expiry
    - `bool` Is it best before or use by?
    - `bool` Is the packaging open?
        - `bool` Individually wrapped and opened or all opened?
- As the developer I want to track the number of days till expiry (`expiryDate - todaysDate`)
- As the developer I want to store the food objects in JSON format
- As the user I want the option to display each food item sorted by different sort filters. These could include:
    - Days till expiry (ascending and descending)
    - Alphabetical
- As a user, I want the option to 'eat' or 'bin' food items to remove them from the list.
- As a user, I want to have a visual indicator as to what food items are approaching expiration. This could use colour coding.
    - Green
        - \> 3 days till expiry
    - Amber
        - <= 3 days till expiry
        - <= 3 days *after* expiry if item is 'best before'
    - Red
        - Any amount of time beyond expiry if item is 'use by'
        - \> 3 days after expiry if item is 'best before'
- As the user I want the expiry date to be updated once the item is opened to a user-defined (*HTML Forms?*) number of days time.
- As the developer I want to use a 'relevant' colour scheme appropriate to the project; as the project is largely based on refrigiration (kinda?) this could be 'cooler' colours such as white / light grey / light blue.
- As the developer I want the option for the user to have access to a toggleable light theme and a dark theme.

```json
{
    "name" : "Milk",
    "expiryDate" : "2023-10-28 00:00:00.000000",
    "isUseBy" : true,
    "isOpen" : true,
    "whenOpenUpdateExpiryDate" : true,
    "individuallyWrapped" : false
}
```
```
int numDays = input()
// When 'isOpen' is updated to true
if not individuallyWrapped and whenOpenUpdateExpiryDate
    expiryDate = todaysDate + days(numDays)
```
```css
:root[data-theme="light"] {
  --text: #130e01;
  --background: #ffffff;
  --primary: #d8d8d8;
  --secondary: #bfffff;
  --accent: #69f6ff;
}
/* If I want to implement light/dark themes */
:root[data-theme="dark"] {
  --text: #fef9ec;
  --background: #000000;
  --primary: #262626;
  --secondary: #004242;
  --accent: #008a94;
}
```

> Or should I track the `openedDate` and `numberOfDaysSinceOpen` so that original `expiryDate` value is maintained?

## Archery Related Ideas

### Archery Range Equipment Manager

This project could be used by archers and archery clubs to track and help maintain the equipment they hold. This would also be useful to track the value of the inventory to assist in insurance calculations, and track the items that may need replacing to help with budgeting.

**Equipment Categories**
- Bows
    - Bowstyle
    - Poundage
    - Left / Right Handed
    - Child objects
        - Accessories
- Arrows
    - Length
    - Spine (optional if known)
    - Quantity
- Accessories
- PPE
    - Type (e.g. wrist guard)
- Workshop
    - Name
- Targets
    - Frames
    - Bosses
    - Target Faces
        - Size
        - Is novelty
- etc.

Information about each item
- Quantity
- Condition

Search by / Sort listings by
- Poundage
- Value
- Storage venue

Sum the total inventory value by
- Storage venue
- Total

> Commonly tracked properties, such as `value`, `storedAtVenue` (`string`) or `dateAcquired`, should be implemented in a base class.

- As a user, I want to be able to add new items via a HTML form.
- As the developer, I want these forms to only display menus related to that object type class. For example `Target Face Size` would not be an input option for `Arrows`.
- As a club treasurer, I want to be able to see the values for equipment stored at each venue/range as well as the combined value to understand what the club has in inventory for insurance calculations.
- As a club treasurer, I want to be able to see the condition of equipment in storage so I can priortise spending towards items that are in direst need of replacing to help with budgeting.
