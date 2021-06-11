# Animal Slaughter House

## brief introduction

- This is a small game that we have implemented based on libgdx.
- The basic idea is a multiplayer online battle royale mode game.
- Basic settings
    - Each player has an initial 100 blood.
    - The default birth point for each player is the bottom left corner of the entire map.
    - Each map has a few drop points where you can pick up a specific weapon. After that , you can use your current weapon to attack other players on the server.
    - The winning condition is to kill all players on the server and the winner is the one who survives to the end.

## main framework

![framework2](framework2.png)

## a  few points to note

### 1. Projectile class


### 2. implementation of online multiplayer


### 3. map plotting &player、item sprite
- map: We use Tiled(https://www.mapeditor.org/)  to make our map, and loaded by class ==TmxMaPLoader==.
- sprites: most pixelated pictures found online. All sprites  are rendered in ==PlayScreen== and some of the sprites are animated by a series of pictures.
### 4. bgm&sound effect

