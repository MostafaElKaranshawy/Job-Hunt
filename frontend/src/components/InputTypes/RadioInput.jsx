import PropTypes from "prop-types";
import { useState } from "react";
import "./inputTypes.css";

export default function RadioInput({ name, options, onChange }) {
    
    const [myradio, setRadio] = useState("");

    const handleRadio = (e) => {
        setRadio(e.target.value);
        onChange(e);
    };

    return (
        <>
        <label className="input-label">{name}</label>
        <br />
        <div className="radio-group">
            {options.map((option, index) => (
                <label key={index}>
                    <input type="radio" value={option} 
                    checked={myradio === option} onChange={handleRadio} />
                    {option}
                </label>
            ))}
        </div>
        </>
    );
}

RadioInput.propTypes = {
    name: PropTypes.string.isRequired,
    options: PropTypes.arrayOf(PropTypes.string.isRequired).isRequired,
    onChange: PropTypes.func,
};
