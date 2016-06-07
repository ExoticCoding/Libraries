# Libs
I've created a simple repository full of libraries I create and I fully intend to add many more as time goes on, many of these are just simple 1 class libs that you can copy into your IDE and use as you please. All of this code is free to use and modify / redistribute at any time.

If you have ideas on what I should add please email me @ swapvinc.6@gmail.com or on skype @ first.last1977 , if you want to make a change create a pull request and I will review it.

## AnvilRecipe
My first lib, AnvilRecipe, is a simple but customizable lib which allows easy use to create recipes for anvils, they are shaped (for now) and they can be amount specific, and meta specific. I'll give a couple examples on how to use it here.

#### In your onEnable:
In your onEnable you'll want to have what is below so the class knows where the main class is so it can register the event (on click listener) so it can apply the recipes.
````java
AnvilRecipe.register(this);
````
#### Wherever you register recipes:
To register recipes it's very simple, to add a recipe you need 3 things, 1: to know if you want it item meta specific or non-item meta specific 2: The three items you want, first will be the left slot, second the right slot, and third the output slot 3: Wether you want it to check item count or not. Registering a recipe would look something like this:
````java
ItemStack[] items = { new ItemStack(Material.STONE), new ItemStack(Material.DIRT), new ItemStack(Material.DIAMOND) };
AnvilRecipe.registerNewRecipe(RecipeType.META_UNSPECIFIC, items);
````
This code would when put in an anvil would turn a piece of stone and a piece of dirt into a piece of diamond. This anvil
