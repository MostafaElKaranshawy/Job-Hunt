import React from "react";

export default function MessageBox({message, setShowMessaegBox}) {
    return (
        <div className="box-container">
            <div className={`confirmation-box`}>
                <p className="content">
                    {message}
                </p>
                <div className="footer">
                    <div className="save-button" onClick={()=> {
                        setShowMessaegBox(false)
                    }}>Ok</div>
                </div>
            </div>
        </div>
    )

}