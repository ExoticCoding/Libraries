# Libs
I've created a simple repository full of libraries I create and I fully intend to add many more as time goes on, many of these are just simple 1 class libs that you can copy into your IDE and use as you please. All of this code is free to use and modify / redistribute at any time.

If you have ideas on what I should add please email me @ swapvinc.6@gmail.com or on skype @ first.last1977 , if you want to make a change create a pull request and I will review it.

## Anvil Recipes
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
## Sign Editor
My second lib, Sign Editor, is a very small and (I think) lightweight solution to opening a sign gui to edit signs in game

#### In your onEnable:
In your onEnable you'll want to have what is below so the lib can respond when the sign gets clicked and so it won't throw a null pointer when you try and open using SignEditor.openEditor(...)
````java
SignEditor.register(this);
````
#### Example usage:
To open a sign gui in code you can do it a number of ways but the one I found the best was using it to edit signs by right clicking a sign and having a gui automatically open up so you can edit it easily, this is how it's done:
````java
	@EventHandler
	public void signRightClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		/*
		 * This makes sure it's a right click on a block and the player has
		 * permission to edit the sign
		 */
		if (event.getAction() != Action.RIGHT_CLICK_BLOCK || !player.hasPermission("sign.edit"))
			return;
		Block block = event.getClickedBlock();
		/*
		 * This line makes sure that the block is a sign
		 */
		if (!(block.getState() instanceof Sign))
			return;
		/*
		 * This will open a sign gui to the target player editing the target
		 * block, when done is pressed it will fire the "onSignFinish(...)" in
		 * the SignResponse, the SignResponse is an interface so the best usage
		 * would be calling a new instance as an anonymous inner
		 */
		SignEditor.openEditor(event.getPlayer(), (Sign) block.getState(), new SignResponse() {
			@Override
			public void onSignFinish(SignChangeEvent event) {
				if (event.getPlayer().hasPermission("sign.color")) {
					for (int i = 0; i < 4; ++i) {
						event.setLine(i, ChatColor.translateAlternateColorCodes('&', event.getLine(i)));
					}
				}
			}
		});
	}
````