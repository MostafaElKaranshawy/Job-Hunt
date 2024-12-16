import { useState } from "react";

export default function InputComponent(){

    const [mytext, setText] = useState("");
    const [textArea, setTextArea] = useState("");
    const [mydate, setDate] = useState("");
    const [myselection, setSelect] = useState();
    const [myradio, setRadio] = useState("");
    const [myBox, setBox] = useState([]);
    const [radioError, setRadioError] = useState("");
    const [checkboxError, setCheckboxError] = useState("");


    function handleTextChange(e){
        setText(e.target.value);
    }
    function handleTextAreaChange(e){
        setTextArea(e.target.value);
    }
    function handleDate(e){
        setDate(e.target.value);
    }
    function handleSelect(e){
        setSelect(e.target.value);
    }
    function handleRadio(e) {
        setRadio(e.target.value);
        setRadioError("");
      }
      function handleBox(e) {
        const value = e.target.value;
        if (myBox.includes(value)) {
          setBox(myBox.filter((box) => box !== value)); // Remove value if already selected
        } else {
          setBox([...myBox, value]); // Add value if not already selected
        }
        if (checkboxError) {
          setCheckboxError(""); // Clear error when at least one checkbox is selected
        }
      }
      function handleFormSubmit(e) {
        e.preventDefault();
        let valid = true;
    
        if (!myradio) {
          setRadioError("Please select an option.");
          valid = false;
        }

        if (myBox.length === 0) {
          setCheckboxError("Please select at least one checkbox.");
          valid = false;
        }
    
        if (valid) {
          alert("Form submitted successfully!");
        }
      }
 
    return(
        <div className="inputs-class">
            <form onSubmit={handleFormSubmit}> 
            <input type="text" value={mytext} onChange={handleTextChange} placeholder="Enter your Label" maxLength={30} required/>
            <br />
            <p className="myp">text input : {mytext}</p>
            <hr />

            <textArea value={textArea} onChange={handleTextAreaChange} placeholder="Enter the Text" required/>
            <br />
            <p className="myp">text area info : {textArea}</p>
            <hr />


            <input type="date" value={mydate} onChange={handleDate} required/>
            <br />
            <p className="myp">date input : {mydate}</p>
            <hr />

            <select onChange={handleSelect} required>
                <option value="">Select a value</option>
                <option value="1">one</option>
                <option value="2">two</option>
                <option value="3">three</option>
            </select>
            <br />
            <p className="myp">my selection : {myselection}</p>
            <hr />
            <div className="radio-group">
                <label>
                    <input type="radio" value="option 1" 
                    checked = {myradio == "option 1"} onChange={handleRadio} />
                    Option 1
                </label>
                <label>
                    <input type="radio" value="option 2" 
                    checked = {myradio == "option 2"} onChange={handleRadio} />
                    Option 2
                </label>
                <label>
                    <input type="radio" value="option 3" 
                    checked = {myradio == "option 3"} onChange={handleRadio} />
                    Option 3
                </label>
                <label>
                    <input type="radio" value="option 4" 
                    checked = {myradio == "option 4"} onChange={handleRadio} />
                    Option 4
                </label>
                <label>
                    <input type="radio" value="option 5" 
                    checked = {myradio == "option 5"} onChange={handleRadio} />
                    Option 5
                </label>
                <label>
                    <input type="radio" value="option 6" 
                    checked = {myradio == "option 6"} onChange={handleRadio} />
                    Option 6
                </label>
                <label>
                    <input type="radio" value="option 7" 
                    checked = {myradio == "option 7"} onChange={handleRadio} />
                    Option 7
                </label>
                <label>
                    <input type="radio" value="option 8" 
                    checked = {myradio == "option 8"} onChange={handleRadio} />
                    Option 8
                </label>
            </div>
            <br />
            {radioError && <p className="error">{radioError}</p>}

            <br />
            <p className="myp">Radio input : {myradio}</p>
            <hr />

            <div className="checkbox-group">
                <label>
                    <input type="checkbox" value="box 1" 
                    checked = {myBox.includes("box 1")} onChange={handleBox} />
                    Box number 1
                </label>
                <label>
                    <input type="checkbox" value="box 2" 
                    checked = {myBox.includes("box 2")} onChange={handleBox} />
                    Box number 2
                </label>
                <label>
                    <input type="checkbox" value="box 3" 
                    checked = {myBox.includes("box 3")} onChange={handleBox} />
                    Box number 3
                </label>
                <label>
                    <input type="checkbox" value="box 4" 
                    checked = {myBox.includes("box 4")} onChange={handleBox} />
                    Box number 4
                </label>
                <label>
                    <input type="checkbox" value="box 5" 
                    checked = {myBox.includes("box 5")} onChange={handleBox} />
                    Box number 5
                </label>
                <label>
                    <input type="checkbox" value="box 6" 
                    checked = {myBox.includes("box 6")} onChange={handleBox} />
                    Box number 6
                </label>
                <label>
                    <input type="checkbox" value="box 7" 
                    checked = {myBox.includes("box 7")} onChange={handleBox} />
                    Box number 7
                </label>
            </div>

            <br />
            {checkboxError && <p className="error">{checkboxError}</p>}

            <br />
            <p className="myp">Checked boxes : {myBox.join(", ")}</p>

            <br /><br /><br /><br />
            <button type="submit">Submit</button>
        </form>
        </div>
    );

}
// export default InputComponent