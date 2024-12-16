import { parse } from "dotenv";

function parseEducation(education){
    return ([
        { fieldName: "Institution", fieldValue: education.institution, fieldType: "text", minLength: 3, maxLength: 20 },
        { fieldName: "Degree", fieldValue: education.degree, fieldType: "text", minLength: 2, maxLength: 20 },
        { fieldName: "Field Of Study", fieldValue: education.fieldOfStudy, fieldType: "text", minLength: 3, maxLength: 50 },
        { fieldName: "Start Date", fieldValue: education.startDate, fieldType: "number", minLength: 4, maxLength: 4 },
        { fieldName: "End Date", fieldValue: education.endDate, fieldType: "number", minLength: 4, maxLength: 4 },
    ]);
}
function parseExperience(experience){
    return ([
        { fieldName: "Title", fieldValue: experience.title, fieldType: "text", minLength: 3, maxLength: 20 },
        { fieldName: "Company", fieldValue: experience.company, fieldType: "text", minLength: 3, maxLength: 50 },
        { fieldName: "Location", fieldValue: experience.location, fieldType: "text", minLength: 3, maxLength: 20 },
        { fieldName: "Start Date", fieldValue: experience.startDate, fieldType: "date", minLength: 0, maxLength: 10 },
        { fieldName: "End Date", fieldValue: experience.endDate, fieldType: "date", minLength: 0, maxLength: 10 },
        { fieldName: "Description", fieldValue: experience.description, fieldType: "textArea", minLength: 3, maxLength: 50 },

    ]);
}

function toCamelCase(str) {
    return str
        .toLowerCase()
        .replace(/(?:^\w|[A-Z]|\b\w)/g, (word, index) =>
            index === 0 ? word.toLowerCase() : word.toUpperCase()
        )
        .replace(/ /g, "");
}

// General parsing function
function parseSection(section) {
    return section?.sectionFields.reduce((acc, field) => {
        acc[toCamelCase(field.fieldName)] = field.fieldValue;
        return acc;
    }, {});
}


export  {
    parseEducation,
    parseExperience,
    toCamelCase,
    parseSection
}