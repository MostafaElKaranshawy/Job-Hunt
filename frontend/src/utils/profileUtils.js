import { parse } from "dotenv";

function parseEducation(educationData){
    return ([
        { fieldName: "Institution", fieldValue: education.institution, fieldType: "text" },
        { fieldName: "Degree", fieldValue: education.degree, fieldType: "text" },
        { fieldName: "Field Of Study", fieldValue: education.fieldOfStudy, fieldType: "text" },
        { fieldName: "Start Date", fieldValue: education.startDate, fieldType: "number" },
        { fieldName: "End Date", fieldValue: education.endDate, fieldType: "number" },
    ]);
}
function parseExperience(educationData){
    return ([
        { fieldName: "Institution", fieldValue: education.institution, fieldType: "text" },
        { fieldName: "Degree", fieldValue: education.degree, fieldType: "text" },
        { fieldName: "Field Of Study", fieldValue: education.fieldOfStudy, fieldType: "text" },
        { fieldName: "Start Date", fieldValue: education.startDate, fieldType: "number" },
        { fieldName: "End Date", fieldValue: education.endDate, fieldType: "number" },
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