const app = require('express')();
const server = require('http').Server(app);
const io = require('socket.io')(server);

let players = {};
let items = [];
let projectiles = [];
let host_id = ""

server.listen(5432,function(){
    console.log("Server is now running...");
});

io.on('connection',  function(socket){
    if(Object.keys(players).length === 0)
        host_id = socket.id;
    console.log("Player Connected!");
    // if there is no player in the server, make this client the host
    socket.emit('isHost', { isHost : Object.keys(players).length !== 0 })
    // self id
    socket.emit('socketID', { id : socket.id });
    // tell client player list
    socket.emit('listPlayers', { players : players });
    // broadcasting self id to everybody else
    socket.broadcast.emit('newPlayer',{ id : socket.id });
    socket.on('pushUpdate', function(data){
        data.id = socket.id;
        players = data.players;
        items = data.items;
        projectiles = data.projectiles;
    });
    // if client disconnects
    socket.on('disconnect', function(){
        console.log("Player Disconnected");
        socket.broadcast.emit('playerDisconnected',{ id : socket.id });
        delete players[socket.id]
    });
    // register new player after registering all services
    players[socket.id] = new player(0, 0);
});

function virtual_entity(x, y, velocity_x, velocity_y){
    this.x = x;
    this.y = y;
    this.dx = velocity_x;
    this.dy = velocity_y;
}

function player(x, y, velocity_x, velocity_y, hp, weapon_on_hand){
    virtual_entity.call(x, y, velocity_x, velocity_y)
    this.hp = hp;
    this.weapon_on_hand = weapon_on_hand;
}

function item(x, y, velocity_x, velocity_y, id){
    virtual_entity.call(x, y, velocity_x, velocity_y)
    this.id = id;
}

function projectile(x, y, velocity_x, velocity_y, attack){
    virtual_entity.call(x, y, velocity_x, velocity_y)
    this.attack = attack;
}