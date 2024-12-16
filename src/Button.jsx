import { useState } from "react";

function Button() {
  const [cnt, setCnt] = useState(1);
  const [isDisabled, setIsDisabled] = useState(false);

  const handleClick = (e) => {

    setIsDisabled(true);
    setCnt(c => c + 1);
    
    e.target.textContent = "Ouch !";
    console.log(`count of clicks = ${cnt}`);
    
    setTimeout(() => {
      e.target.textContent = "click here 😀";
      setIsDisabled(false);
    }, 2000);
    
  };

  return (
    <button
      className="my-button"
      onDoubleClick={handleClick}
      disabled={isDisabled}
    >
      click here 😀
    </button>
  );
}

export default Button;
