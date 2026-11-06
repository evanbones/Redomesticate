# Redomesticate

<a href='https://fabricmc.net'><img alt="fabric" height="56" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/supported/fabric_vector.svg"></a>
<a href='https://neoforged.net/'><img alt="neoforge" height="56" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/supported/neoforge_vector.svg"></a>

Redomesticate is a multiloader port and partial remake of Domestication Innovation, which makes tamable mobs in
Minecraft more useful and engaging by giving massive improvements to how tamed mobs behave and interact with the world.
There are several new items and blocks to improve the tamed mob experience and a new Pet Enchanting system. Aside from
the new command system, most of these changes are reflected in both vanilla mobs and new tamed mobs introduced in other
mods.

![A wolf with enchanted collar overlays](https://raw.githubusercontent.com/evanbones/Redomesticate/1.21.1/images/wolf_enchanted.png)

---

## What's New?

Redomesticate has been brought into the modern modding era with massive changes:

**Multiloader Support:**

Now officially available on **1.21.1/26.x** for both **NeoForge** and **Fabric**!

**No More Citadel!**

Citadel has been completely stripped out for a cleaner experience with no mandatory dependencies.

**Data-Driven Mechanics:**

Conversions (like Horses → Zombie Horses) and taming are now **fully data-driven**, giving modpack makers and datapack
authors enormous flexibility. For example, tameable zombies with Rotten Flesh is a single datapack .json file. Supports
any taming item and chance for any mob-to-mob conversion.

**Jade Integration:**

Compatibility with Jade allows for detailed tooltips showing your pet's active enchantments and pet bed location at a
glance.

---

## Feature Breakdown

### Wander, Stay, Follow

Wolves, Cats, Parrots, Axolotls, Rabbits, and Foxes now have 3 possible states when tamed: **Wander**, **Stay**, and
**Follow**. These are cycled through by right-clicking on the mob. Now it's finally possible to have your cats and dogs
wander around your house instead of being frozen in place or randomly teleporting to you. This feature is configurable.

### Swing Through Pets

Unless sneaking, the swing of your sword, fist, or any attack will pass right through any of your tamed pets, hitting
any possible mob behind the pet as well. This means you're free to get in the battle with your pets without fear of
hurting them! This applies to arrows and other projectiles too. If you do want to hit a tamed mob, hold sneak during the
attack. Configurable.

### Sweeping Edge Changes

Sweeping Edge can no longer hurt any of your tamed pets.

### Tameable Axolotls

Gone are the days of weirdly holding a tropical fish bucket if you want an Axolotl as a companion. These amphibians are
now fully tameable: simply feed a wild axolotl tropical fish from a bucket to tame them. They can be made to sit,
wander, and follow like all the other tameable mobs. Note that axolotls are by default very slow on land, dry out, and
have low health — you've been warned. Configurable.

### Tameable Foxes

Tamed foxes are now actually tamed and can be made to sit, wander and follow like the other pets. To get a tame fox,
breed two wild foxes with sweet berries and wait for the kit to grow up. No matter how tame foxes are, they'll still
want to kill any chickens and (untamed) rabbits that they see. Configurable.

### Tameable Rabbits

When rabbits were initially added to the game, they were fully tameable just like cats and wolves — this was eventually
removed. This change has been undone. To tame a rabbit, feed it several Hay Bales. Rabbits only have 3 health by default
and *usually* cannot attack (see: Sinister Carrot). To make them useful, Redomesticate reintroduces another removed
feature: **rabbits scare off Ravagers**. Both tamability and the scaring of Ravagers are configurable.

### Tameable Frogs

Now you can tame Frogs in Minecraft with a few spider eyes. Frogs can be made to stay, follow, or wander like the other
pets, and will come to their owner's defense. They may have low attack and health, but they make up for it in
cuteness.

### Pet Beds

Pet Beds are craftable blocks that come in all 16 dye colours.

![Pet Bed recipe](https://raw.githubusercontent.com/evanbones/Redomesticate/1.21.1/images/pet_bed_recipe.png)

When a tamed mob walks onto a Pet Bed, it will attempt to claim it as its own — indicated by ZZZ particles appearing
above the pet's head. Each bed can only be claimed by one pet at a time, and nearby pets without a bed will
automatically seek out and navigate to an unclaimed bed within range.

Once claimed, if the pet dies, it will respawn at its bed at the next dawn. Pet enchants are preserved on respawn. To
release a bed, simply break it. Configurable.

### Wayward Lantern

The Wayward Lantern is a decorative block crafted from iron ingots and a lantern, and features a newly reworked model
and texture.

![Wayward Lantern](https://raw.githubusercontent.com/evanbones/Redomesticate/1.21.1/images/wayward_lantern.png)

![Wayward Lantern recipe](https://raw.githubusercontent.com/evanbones/Redomesticate/1.21.1/images/wayward_lantern_recipe.png)

If you are ever so far away from your pets in Follow mode that they unload from the world, they will eventually find
their way back to the closest Wayward Lantern to you. This makes it perfectly fine to die far from home without losing
both your pets and your loot.

### Feather on a Stick

The Feather on a Stick is a simple tool crafted like so:

![Feather on a Stick recipe](https://raw.githubusercontent.com/evanbones/Redomesticate/1.21.1/images/feather_recipe.png)

When used, it sends out a feather on a string in front of you. The closest pet of yours will then try to move onto the
feather to play with it. This is useful for getting a mob out of places it shouldn't be (stuck in a doorway, on top of a
chest).

### Rotten Apple & Sinister Carrot

**Rotten Apples** have a chance to form if an apple item despawns on the ground (configurable). Feeding one to a horse
turns it into a Zombie Horse, preserving its speed and other stats. Zombie horses cannot wear armor.

**Sinister Carrots** are rarely found as loot in Woodland Mansions (configurable). They can be fed to a Zombie Horse to
convert it into a Skeleton Horse, or fed to a rabbit to transform it into a **Killer Rabbit** — which can attack and is
much stronger than a normal rabbit.

### Animal Tamer Villager

Animal Tamers can be found in villages in their own unique houses (spawn rate configurable) or can be created from an
unemployed villager using a Pet Bed as a workstation. They trade a variety of animal-related items from both vanilla
Minecraft and this mod, including unique pet enchants. Their houses contain untamed mobs and sometimes fish tanks.

The Animal Tamer villager can be disabled via datapacks.

### Deed of Ownership

The Deed of Ownership is an item purchasable from the Animal Tamer villager. Right-clicking on a tamed pet you own binds
it to the deed. Giving the deed to another player and having them use it on the bound animal transfers ownership,
as if they had tamed the animal themselves.

### Command Drum

The Command Drum automates commanding groups of mobs via redstone.

![Command Drum recipe](https://raw.githubusercontent.com/evanbones/Redomesticate/1.21.1/images/drum_recipe.png)

When interacted with, the drum beats and changes command modes, ordering all your tamed mobs to follow, stay, or wander
depending on the beat. If given a redstone signal, it commands the mobs of the player who originally placed the block.
The block also makes a funny sound if you jump on it.

### Collar Tags

Collar Tags are the conduit for pet enchantments.

![Collar Tag recipe](https://raw.githubusercontent.com/evanbones/Redomesticate/1.21.1/images/collar_tag_recipe.png)

Like a Name Tag, Collar Tags can be renamed in an anvil and used on a mob to rename it (only for your own pets, unlike
Name Tags). Their real power is that they can be enchanted (configurable).

### Enchanting Your Pets

Pets are enchanted by applying enchants to a Collar Tag in either an anvil or an enchanting table, then placing the
collar on your tamed pet by right-clicking. To retrieve an enchanted collar, swap it with any other collar (even an
unenchanted one). You can view a pet's enchants by sneaking and looking at them, or via Jade if it's installed.

![Enchanting table with collar tag](https://raw.githubusercontent.com/evanbones/Redomesticate/1.21.1/images/enchanting_table.gif)

---

## All Pet Enchantments

35 Pet Enchantments are added in total, including 4 curses and 5 loot/trade-exclusive enchantments:

![Full enchantment list](https://raw.githubusercontent.com/evanbones/Redomesticate/1.21.1/images/list_of_enchantments.png)

```
Health Boost I-III        : Pet gains 10 additional maximum health per level.
Fireproof                 : Pet cannot be damaged by fire or lava.
Immunity Frame I-III      : Pet gains immunity frames (1 second per level) after being attacked,
                            shown by an energy barrier overlay. Pet takes no further damage during this window.
Deflecting                : Pet is protected by a ghostly shield which deflects any projectile attacks.
Poison Resistance         : Pet cannot be inflicted with the Poison effect.
Chain Lightning I-II      : When attacking, pet shoots lightning that arcs from the target to nearby mobs.
                            Level determines how many mobs the lightning arcs between.
Speedster I-III           : Increases pet's movement speed.
Frost Fang                : Inflicts attack targets with the frozen effect, slowing them and damaging them over time.
Magnetic                  : Pet is aided by a ghostly magnet which pulls attack targets closer to it.
Linked Inventory          : Pet can pick up items and teleport them directly to its owner's inventory.
Total Recall              : When below 2 health, pet enters a recall orb and is protected from all damage
                            until released by its owner.
Health Siphon             : Transfers any incoming damage from the pet to its owner, if nearby.
Bubbling I-II             : Pet attacks trap the target inside a giant bubble which floats upward and pops,
                            sending the target plummeting. Level determines bubble duration.
                            Only found as loot in Buried Treasure.
Herding I-II              : Pet can be followed by untamed animals. Level determines max followers.
Amphibious                : Pet cannot dry out (if aquatic) or drown (if a land animal).
                            Improves underwater movement and allows land animals to properly swim.
Vampire's Familiar I-II   : Pet regains a portion of health per successful attack.
                            Level I = 50%, Level II = 100%. Only found in Woodland Mansions.
Void Cloud                : If pet falls off a cliff or into the void, a cloud lifts it back toward its owner.
                            Only found in End Cities.
Charisma I-III            : Lowers villager trade prices when nearby owner.
                            Level determines discount amount. Only given as a villager trade.
Shadow Hands I-IV         : Pet gains dark magical shadow hands that deal melee damage at range.
                            Level determines count and speed of hands when attacking.
Disc Jockey               : Pet is followed by a magical jukebox that can play any record on loop.
Defusal I-III             : Pet will nullify nearby explosions (creepers, TNT, etc.).
                            Level determines range of effect.
Warping Bite              : Pet teleports its target away when attacking.
Ore Scenting I-III        : Pet occasionally sniffs out local ores and highlights them to nearby players.
                            Level determines frequency, range, and amount highlighted.
                            Only found in Abandoned Mineshafts.
Gluttonous                : Pet can eat any food when low on health to heal, ignoring dietary restrictions
                            and negative food effects.
Psychic Wall I-III        : Pet summons a psychic energy wall between its owner and nearby monsters during
                            combat, stopping unfriendly entities from walking through it.
                            Level determines wall size, duration, and frequency.
Intimidation I-II         : Pet occasionally scares all nearby monsters away from it.
                            Level determines frequency and range.
Tethered Teleport         : Pet automatically teleports with its owner whenever the owner teleports,
                            including through portals, via commands, ender pearls, and more.
Muffled                   : Pet emits no sound or vibrations. Only found in Ancient Cities.
Blazing Protection I-III  : Pet is surrounded by blaze bars (2 per level) which absorb incoming damage
                            and ignite attackers. Only found in Nether Fortresses.
Healing Aura I-II         : When this pet or its allies are injured, it creates a circle of regeneration
                            around itself, healing nearby mobs.
Rejuvenation              : Pet absorbs nearby experience orbs when injured to convert them to health.
Curse of Afterlife        : If the pet is slain, a hostile zombified version appears with all of its
                            enchants and abilities.
Curse of Infamy           : Pet attracts attention from nearby monsters, which will begin targeting it.
Curse of Blight           : Random flowers, crops, and grass blocks occasionally wither and die near this pet.
Curse of Immaturity       : Pet appears as a baby animal and deals less damage when attacking.
```

Most enchants are mutually exclusive with certain others and have varying rarity. See the enchantment chart above for
full compatibility details.

---

## A Word on Mod Compatibility

The Wander, Stay, and Follow command system works automatically for any mod mob that uses standard goal-based AI.
Brain-based AI mobs (like axolotls and frogs) need to be tagged with `redomesticate:uses_brain_ai`;
vanilla examples are handled automatically, but other mods' brain-based mobs may have limited trinary command support.
All other features (pet beds, enchants, deed of ownership, etc.) work with any tameable mob from any mod.

---

## Special Thanks & Credits

- **AlexModGuy**, without whom this mod wouldn't be possible.
- **Kobber** for the incredible new Wayward Lantern model and textures.

---

## License

[![Code license (GPL3.0)](https://img.shields.io/badge/code%20license-GPL3.0-green.svg?style=flat-square)](https://github.com/evanbones/Redomesticate/blob/1.21.1/LICENSE)

---

[![discord-plural](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/social/discord-plural_vector.svg)](https://discord.com/invite/JcGRdT6Pbx) [![github-plural](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/social/github-plural_vector.svg)](https://github.com/evanbones/Redomesticate)