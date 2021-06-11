# Animal Slaughter House

## Brief introduction

- A primitive 2D game implemented with LibGDX.
- Features: Multiplayer, online battle-royale.
- Basic settings
    - Each player has 100 HP at the beginning.
    - Every player will be spawned in the bottom left corner of the entire map.
    - There are a few drop points scattered in the world where you can pick up a specific weapon. After that, you can use your current weapon to attack other players on the server.
    - Kill all players on the server and survive to the end to win!

## Main Framework

![framework2](framework2.png)

## A few points to note

### 1. Projectile class as an example of sprite

- Everything shown in the screen is a sprite.
- Projectile inherits from Sprite class of LibGDX.

### 2. implementation of online multiplayer

We used socket.io to implement our online multiplayer.
We made the server using node.js to simplify the work.
There is a host that actually update the world,
other players(slaves) only listen to the host and update
from the host.
For every frame, the host pack sprites in the world into 
a json file to synchronize all clients.

### 3. drawing map and sprites

LibGDX offers a way to create textures for sprites.
For each frame sprites are batched and rendered.

### 4. bgm & sound effect

Implemented using native libraries from LibGDX.