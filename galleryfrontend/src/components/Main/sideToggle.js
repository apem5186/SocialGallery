function sideToggle(){
        const container = document.getElementById("nav");
        const close = document.getElementById("close");
    
        const toggle = () => {
        container.classList.toggle("close");
        };
    
        close.addEventListener("click", toggle);
}
export default sideToggle;