import profile from "./assets/pic.jpg"

function Card({name, info}){

    return (
        <div className="card">
            <img className = "card-image" src = {profile} alt="Profile Photo"></img>
            <h1 className = "card-title">{name}</h1>
            <p className = "card-text">{info}</p>
        </div>
    );
}

export default Card