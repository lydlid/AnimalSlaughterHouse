const app = require('express')();
const server = require('http').Server(app);
const io = require('socket.io')(server);

// update respectively
let players_box2d = {};
// update by host
let players_attributes = {};
let items = [];
let projectiles = [];
let host_id = ""

server.listen(5432,function(){
    console.log("Server is now running...");
});

io.on('connection',  function(socket){
    if(Object.keys(players_box2d).length === 0) {
        host_id = socket.id;
        console.log("We have a host now!");
    }
    console.log("Player Connected!");
    // if there is no player in the server, make this client the host
    socket.emit('isHost', { isHost : Object.keys(players_box2d).length === 0 })
    // self id
    socket.emit('socketID', { id : socket.id });
    players_box2d[socket.id] = new player_box2d(64, 64, 0, 0);
    players_attributes[socket.id] = new player_attribute(100, 0);
    // tell other clients a new player comes
    socket.broadcast.emit('newPlayer',{ id : socket.id, player_box2d : players_box2d[socket.id], player_attribute : players_attributes[socket.id] });
    // tell this client to initialize its own player
    socket.emit('selfPlayer',{ id : socket.id, player_box2d : players_box2d[socket.id], player_attribute : players_attributes[socket.id] });
    // when client requests update from server, this only happens when someone joins the world
    socket.on('requestWorld', function(){
        socket.emit('fullWorld', { players_box2d : players_box2d, players_attribute : players_attributes, items : items, projectiles : projectiles });
    });
    // get host's update
    socket.on('hostUpdate', function (data){
        players_box2d = data.players_box2d;
        items = data.items;
        projectiles = data.projectiles;
    });

    socket.on('slaveUpdate', function (data){
        players_attributes[socket.id] = data;
    })

    socket.on('newProjectile', function (data){
        socket.broadcast('newProjectile', data);
    });

    // if client disconnects
    socket.on('disconnect', function(){
        console.log("Player Disconnected");
        socket.broadcast.emit('playerDisconnected',{ id : socket.id });
        delete players_box2d[socket.id]
        delete players_attributes[socket.id]
    });
});

function virtual_entity(x, y, velocity_x, velocity_y){
    this.x = x
    this.y = y;
    this.velocity_x = velocity_x;
    this.velocity_y = velocity_y;
}

function player_box2d(x, y, velocity_x, velocity_y){
    this.x = x
    this.y = y;
    this.velocity_x = velocity_x;
    this.velocity_y = velocity_y;
}

function player_attribute(hit_point, weapon_on_hand){
    this.hit_point = hit_point;
    this.weapon_on_hand = weapon_on_hand;
}

function item(x, y, velocity_x, velocity_y, id){
    virtual_entity.call(x, y, velocity_x, velocity_y);
    this.id = id;
}

function projectile(x, y, velocity_x, velocity_y){
    virtual_entity.call(x, y, velocity_x, velocity_y);
    this.id = id;
}