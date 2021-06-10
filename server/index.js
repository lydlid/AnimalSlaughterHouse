const app = require('express')();
const server = require('http').Server(app);
const io = require('socket.io')(server);

// update respectively
let players_box2d = {};
// update by host
let players_attribute = {};
let players_count = 0;
let items = [];
let projectiles = [];
let host_id = ""

server.listen(5432,function(){
    console.log("Server is now running...");
});

io.on('connection',  function(socket){
    if(players_count === 0) {
        host_id = socket.id;
        console.log("We have a host now!");
        // if there is no player in the server, make this client the host
        socket.emit('isHost', {isHost : true});
    }
    players_count++;
    console.log("Player Connected!");

    // self id
    socket.emit('socketID', { id : socket.id });
    players_box2d[socket.id] = {x : 64, y : 64, velocity_x : 0, velocity_y : 0};
    players_attribute[socket.id] = {hit_point : 100, weapon_on_hand : 0};

    // tell other clients a new player comes
    socket.broadcast.emit('newPlayer',{ id : socket.id, player_box2d : players_box2d[socket.id], player_attribute : players_attribute[socket.id] });

    // when client requests update from server, this only happens when someone joins the world
    socket.on('requestWorld', async function () {
        await new Promise(resolve => setTimeout(resolve, 200));
        socket.emit('fullWorld', {
            players_box2d: players_box2d,
            players_attribute: players_attribute,
            items: items,
            projectiles: projectiles
        });
    });

    // get host's update
    socket.on('hostUpdate', function (data) {
        players_attribute = data.players_attribute;
        items = data.items;
        projectiles = data.projectiles;
        socket.broadcast.emit('slaveUpdate', { players_box2d : players_box2d, players_attribute : players_attribute, items : items, projectiles : projectiles });
        socket.emit('hostUpdate',{ players_box2d : players_box2d})
    });

    socket.on('slaveUpdate', function(data){
        //console.log(data);
        players_box2d[socket.id] = data;
        //console.log(players_box2d);
    })

    socket.on('newProjectile', function (data){
        //socket.broadcast.emit('newProjectile', data);
        io.to(host_id).emit('newProjectile', data);
    });

    // if client disconnects
    socket.on('disconnect', function(){
        let id = socket.id;
        // host disconnected
        if(id === host_id) {
            let new_host_id = Object.keys(players_box2d)[Math.floor(Math.random()*Object.keys(players_box2d).length)];
            io.to(new_host_id).emit('isHost', {isHost : true});
            console.log(new_host_id);
            host_id = new_host_id;
        }
        console.log(id + " Disconnected");
        socket.broadcast.emit('playerDisconnected',{ id : id });
        delete players_box2d[id]
        delete players_attribute[id]
        players_count--;
    });
});

// function virtual_entity(x, y, velocity_x, velocity_y){
//     this.x = x
//     this.y = y;
//     this.velocity_x = velocity_x;
//     this.velocity_y = velocity_y;
// }
//
// function player_box2d(x, y, velocity_x, velocity_y){
//     this.x = x
//     this.y = y;
//     this.velocity_x = velocity_x;
//     this.velocity_y = velocity_y;
// }
//
// function player_attribute(hit_point, weapon_on_hand){
//     this.hit_point = hit_point;
//     this.weapon_on_hand = weapon_on_hand;
// }
//
// function item(x, y, velocity_x, velocity_y, id){
//     virtual_entity.call(x, y, velocity_x, velocity_y);
//     this.id = id;
// }
//
// function projectile(x, y, velocity_x, velocity_y){
//     virtual_entity.call(x, y, velocity_x, velocity_y);
//     this.id = id;
// }