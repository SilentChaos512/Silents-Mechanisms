# Silent's Mechanisms

A simple technology mod for Minecraft 1.14+. This mod is not intended to be especially complex right now, that may or may not change in the future.

All machines run on **Forge Energy (FE)** and have proper **recipe systems**. Recipes can be added with data packs. See the wiki for details on creating recipes.

## Downloads/Links

Download the mod only on CurseForge: https://www.curseforge.com/minecraft/mc-mods/silents-mechanisms

## Blocks

- **Alloy Smelter** - A machine that takes up to four inputs and outputs one item. Recipes can be added with data packs.
- **Battery Box** - Stores Forge Energy (FE). Capacity increases when extra batteries are installed. The charged batteries can be removed and used elsewhere.
- **Coal Generator** - Creates Forge Energy (FE) from furnace fuels.
- **Compressor** - A machine that has one input and one output. Can consume more than one of the input item per recipes. Recipes can be added with data packs.
- **Crusher** - A machine that has one input and can produce up to four outputs. Outputs can have a probability assigned. The mod includes recipes for doubling common ores. Recipes can be added with data packs.
- **Electric Furnace** - A machine that works with both blasting and smelting (regular furnace) recipes. Blasting recipes are given priority.
- **Wires** - Used to transfer energy (currently not working properly...)

## Making an Add-on

To use Silent's Mechanisms in your project, you need to add dependencies for Silent's Mechanisms, Silent Lib, and silent-utils. Add the following to your `build.gradle`.

You alse need to generate a GitHub token and add it along with your GitHub username to your personal `gradle.properties` file in `C:\Users\YOUR_USERNAME\.gradle` or `~/.gradle/gradle.properties`. This file may not exist, and you would have to create it yourself.

GitHub tokens can be generated [here](https://github.com/settings/tokens). Click _Generate New Token_ and click the checkmark for _read:packages_

Example of `gradle.properties` file in `C:\Users\YOUR_USERNAME\.gradle` or `~/.gradle/gradle.properties`

```gradle
//Your GitHub username
gpr.username=SilentChaos512

// Your GitHub generated token (bunch of hex digits) with read permission
gpr.token=paste_your_token_here
```

-----------------------------------

Code to add to `build.gradle`. _Note that "silentlib" is not hyphenated. I was following a different naming convention when the repo was created._

I prefer to assign my authentication details to a variable to reduce duplication and make the build file look cleaner.

```gradle
// Authentication details for GitHub packages
// This can also go in the `repositories` block or you can inline it if you prefer
def gpr_creds = {
    username = property('gpr.username')
    password = property('gpr.token')
}
```

Add all the necessary repositories...

```gradle
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/silentchaos512/silents-mechanisms")
        credentials gpr_creds
    }
    maven {
        url = uri("https://maven.pkg.github.com/silentchaos512/silentlib")
        credentials gpr_creds
    }
    maven {
        url = uri("https://maven.pkg.github.com/silentchaos512/silent-utils")
        credentials gpr_creds
    }
}
```

And finally, add dependencies for Silent's Mechanisms and Silent Lib (which will include silent-utils for you).

```gradle
dependencies {
    // Replace VERSION with the version you need, in the form of "MC_VERSION-MOD_VERSION"
    // Example: compile fg.deobf("net.silentchaos512:silents-mechanisms:1.16.3-1.+")
    // Available builds can be found here: https://github.com/SilentChaos512/silents-mechanisms/packages
    // The "exclude module" lines will prevent import errors in some cases
    compile fg.deobf("net.silentchaos512:silents-mechanisms:VERSION") {
        exclude module: 'forge'
        exclude module: 'jei-1.16.5'
        exclude module: 'silent-lib-1.16.3'
    }

    // Same as before, VERSION is in the form "MC_VERSION-MOD_VERSION" (eg, 1.16.3-4.+)
    // https://github.com/SilentChaos512/silentlib/packages
    compile fg.deobf("net.silentchaos512:silent-lib:VERSION") {
        exclude module: "forge"
    }
}
```
