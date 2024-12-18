import React, { useState, useEffect } from "react";
import EmailInput from "../InputTypes/EmailInput";
import PhoneInput from "../InputTypes/PhoneInput";
import SimpleText from "../InputTypes/SimpleText";
import TextArea from "../InputTypes/TextArea";
import DateInput from "../InputTypes/DateInput";
import URLInput from "../InputTypes/URLInput";

export default function PersonalSection({ onChange }) {
    const [fullName, setFullName] = useState("");
    const [phoneNumber, setPhoneNumber] = useState("");
    const [personalEmail, setPersonalEmail] = useState("");
    const [address, setAddress] = useState("");
    const [dateOfBirth, setDateOfBirth] = useState("");
    const [linkedInURL, setLinkedInURL] = useState("");
    const [portfolioURL, setPortfolioURL] = useState("");

    useEffect(() => {
        onChange({
            fullName,
            phoneNumber,
            personalEmail,
            address,
            dateOfBirth,
            linkedInURL,
            portfolioURL,
        });
    }, [fullName, phoneNumber, personalEmail, address, dateOfBirth, linkedInURL, portfolioURL]);

    return (
        <div className="section-container">
            <h2 className="section-header">Personal Information</h2>
            <div className="section">
                <SimpleText name="Full Name" value={fullName} onChange={setFullName} />
                <br />

                <PhoneInput name="Phone Number" value={phoneNumber} onChange={setPhoneNumber} />
                <br />

                <EmailInput name="Personal Email" value={personalEmail} onChange={setPersonalEmail} />
                <br />

                <TextArea name="Address" value={address} onChange={setAddress} isMust={false} />
                <br />

                <DateInput name="Date of Birth" value={dateOfBirth} onChange={setDateOfBirth} isMust={false} />
                <br />

                <URLInput name="LinkedIn URL" value={linkedInURL} onChange={setLinkedInURL} isMust={false} />
                <br />

                <URLInput name="Portfolio / Website" value={portfolioURL} onChange={setPortfolioURL} isMust={false} />
            </div>
        </div>
    );
}