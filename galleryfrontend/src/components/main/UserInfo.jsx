function UserInfo(){

    const layer_button = document.getElementById('layer_button');
    const layer = document.getElementById('layer');
    layer_button.addEventListener('click', function(e){
        layer.setAttribute('style', "display:flex")
    });

    layer.addEventListener("click", function(e){
        layer.setAttribute('style', "display:none")
    })


}

export default UserInfo;