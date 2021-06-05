var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);

server.listen(5432,function(){
    console.log("Server is now running...");
});

io.on('connection',  function(socket){
    console.log("Player Connected!");
    socket.emit('socketID',{ id:socket.id });
    socket.broadcast.emit('newPlayer',{  id:socket.id});
    socket.on('disconnect',  function(){
        console.log("Player Disconnected");
    });
});