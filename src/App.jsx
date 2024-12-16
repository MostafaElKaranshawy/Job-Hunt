import SpecialForm from "./components/specialForm/SpecialForm.jsx";
import CreateForm from "./components/createForm/CreateForm.jsx";
import CreateJob from "./components/CreateJob/createJob.jsx";

function App() {
  
  const myData = {
    sections: [
        {
            sectionName: "Personal Information",
            labels: ["First Name", "Last Name", "Date of Birth", "Gender", "Address"],
            fieldType: ["text", "text", "date", "radio", "text"],
            fieldOptions: [
                [], // No options needed for text fields
                [], // No options needed for text fields
                [], // No options needed for date field
                ["Male", "Female"], // Dropdown options for Gender
                []  // No options needed for address field
            ],
            Mandatory: [true, true, true, true, false]
        },
        {
            sectionName: "Contact Information",
            labels: ["Email Address", "Phone Number", "Alternate Phone Number", "Preferred Contact Method"],
            fieldType: ["email", "phone", "phone", "dropdown"],
            fieldOptions: [
                [], // No options needed for text fields
                [], // No options needed for text fields
                [], // No options needed for text fields
                ["Email", "Phone", "None"] // Dropdown options for contact method
            ],
            Mandatory: [true, true, false, true]
        },
        {
            sectionName: "Education Details",
            labels: ["Highest Qualification", "Field of Study", "University/Institution", "Graduation Year"],
            fieldType: ["dropdown", "text", "text", "date"],
            fieldOptions: [
                ["High School", "Bachelor's", "Master's", "PhD"], // Dropdown options for qualification
                [], // No options needed for field of study
                [], // No options needed for university/institution
                []  // No options needed for graduation year
            ],
            Mandatory: [true, true, true, true]
        },
        {
            sectionName: "Work Experience",
            labels: ["Job Title", "Company Name", "Job Location", "Start Date", "End Date", "Skills"],
            fieldType: ["text", "text", "dropdown", "date", "year", "checkbox"],
            fieldOptions: [
                [], // No options needed for text fields
                [], // No options needed for text fields
                ["Onsite", "Hybrid", "Remote"], // Dropdown options for job location
                [], // No options needed for date fields
                [],  // No options needed for date fields
                ["Java", "C++", "Python", "Kotlin", "React"]
            ],
            Mandatory: [true, true, true, true, false, false]
        }
    ],
    staticSections: [
      "Personal Information",
      "Education",
      "Experience",
      "Skills"
    ],

    fields: [
        {
            label: "LinkedIn Profile",
            fieldType: "url",
            fieldOptions: [],
            isMandatory: false
        },
        {
            label: "GitHub Profile",
            fieldType: "url",
            fieldOptions: [],
            isMandatory: false
        },
        {
            label: "Portfolio Website",
            fieldType: "url",
            fieldOptions: [],
            isMandatory: false
        },
        {
            label: "Cover Letter",
            fieldType: "textarea",
            fieldOptions: [],
            isMandatory: false
        },
        {
            label: "Preferred Interview Date",
            fieldType: "date",
            fieldOptions: [],
            isMandatory: true
        }
    ]
};
       
  return (
    <>
    <SpecialForm sectionData={myData} />
    <br />
    {/* <CreateForm /> */} 
    <CreateJob />
    </>
  );
}

export default App

{/* <form>
      <SpecialSection sectionName={sampleData.sectionName} labels={sampleData.labels} 
      fieldType={sampleData.fieldType} fieldOptions={sampleData.fieldOptions} Mandatory={sampleData.Mandatory} />

      <button type="submit">Send</button>
    </form>  */}
// const sampleData = {
//   sectionName: "Special Section",
//   labels: ["Your Name", "Email Address", "Phone Number", "Gender", "Country"],
//   fieldType: ["text", "email", "phone", "dropdown", "dropdown"],
//   fieldOptions: [
//       [], // No options needed for text fields
//       [], // No options needed for text fields
//       [], // No options needed for text fields
//       ["Male", "Female", "Other"], // Dropdown options for Gender
//       ["USA", "Canada", "UK", "Australia", "Other"] // Dropdown options for Country
//   ],
//   Mandatory: [true, true, false, true, false] // Indicates which fields are mandatory
// };

 // const cardData = [
  //   { name: "Mustafa Kamel", description: "I am a thrid year Computer Engineering Student in Alexandria University at CSED"},
  //   { name: "Ali Ahmed", description: "Mechanical Engineering Student" },
  //   { name: "Sara Ali", description: "Electrical Engineering Student" },
  //   { name: "John Smith", description: "Business Management Student" },
  //   { name: "Jane Doe", description: "Biology Student" },
  //   { name: "Ahmed Khaled", description: "Mathematics Student" },
  //   { name: "Laila Mona", description: "Physics Student" },
  //   { name: "Youssef Hassan", description: "Art & Design Student" },
  // ];

  // const cardList = cardData.map((data) => (
  //   <Card key={data.name} name={data.name} info={data.description} /> ))

  // const options = ["one","Two","Three","4","5","6","7"];

{/* <div className="card-container">
        {cardList}
      </div>

      <br />
      <>
        <div className="user">
          <UserGreeting islogged={true} name={cardData[0].name} info = {cardData[1].description}/>
        </div>

        <br />
        <Button />
        
        <br /><br />
        <InputComponent />
      </> */}

{/* <div className="education-section">
          <form> 
            
            <SimpleText name = {"Name"} isMust={false} />
            <br />
            <hr />

            <TextArea name = {"Info"} isMust={false} />
            <br />
            <hr />

            <DateInput name = {"DATE"} isMust={false} />
            <br />
            <hr />

            <DropDown name={"Select option"} options={options} isMust={false} />
            <br />
            <hr />

            <RadioInput name={"Select Radio"} options={options} />
            <br />
            <hr />

            <CheckboxInput name={"Select Boxes"} options={options} />
            <br />
            <hr />
            
            <URLInput name={"LinkedIn URL"} isMust={false} />
            <br />
            <hr />

            <EmailInput name={"Your Email"} isMust={false} />
            <br />
            <hr />
            
            <PhoneInput name={"Phone Numebr"} />
            <br />
            <hr /> 

            <br /><br />
            <button type="submit"> Submit </button>
          </form>
        </div> */}
