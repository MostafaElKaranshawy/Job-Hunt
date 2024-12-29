import PropTypes from "prop-types";
import { useState } from "react";
import "./inputTypes.css";

export default function URLInput({ name, isMust,value, onChange }) {
    const [myURL, setURL] = useState("");

    function handleURLChange(e) {
        onChange(e.target.value);
    }

    return (
        <div className="url-input">
            <label className="input-label">{name}</label>
            <br />
            <input type="url" value={value} onChange={handleURLChange}
                placeholder="Enter URL" maxLength={200} required={isMust} />
        </div>
    );
}

URLInput.propTypes = {
    name: PropTypes.string.isRequired,
    isMust: PropTypes.bool,
    onChange: PropTypes.func,
    value: PropTypes.string,
};

URLInput.defaultProps = {
    isMust: true,
};
