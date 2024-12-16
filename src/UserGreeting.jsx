function UserGreeting(props){

    const welcome  = <div className="greetingOk">
                        Welcome User {props.name}: &nbsp; {props.info}
                    </div>

    const deny = <div className="greetingBad">
                    sorry user
                </div>
    
    return props.islogged ? welcome : deny;
}

export default UserGreeting