import {get} from 'lodash';

const PATTERN_TO_CHECK_FILE_EXTENSION = "image/(png|jpg|jpeg)";
const PATTERN_TO_CHECK_URL = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

export const isEmptyValidation = (fields, pathNames) => {
    const result = {};
    if (!fields) {
        pathNames.forEach(emptyPath => result[emptyPath] = '* Field is empty');
        return result;
    }
    pathNames
        .filter(path => !fields[path])
        .forEach(emptyPath => result[emptyPath] = '* Field is empty');
    return result;
}

export const isEmptyArrayValidation = (fields, pathNames) => {
    const result = {};
    if (!fields) {
        pathNames.forEach(emptyPath => result[emptyPath] = '* Field is empty');
        return result;
    }
    pathNames
        .filter(path => !fields[path] || fields[path].length === 0)
        .forEach(emptyPath => result[emptyPath] = '* Field is empty');
    return result;
}

export const regexValidation = (fields, pathNames, regex) => {
    const result = {};
    if (!fields) {
        return result;
    }
    let regexChecker = new RegExp(regex);
    pathNames
        .filter(path => !regexChecker.test(fields[path]))
        .forEach(incorrectPath => result[incorrectPath] = '* Incorrect field value');
    return result;
}

export const lengthValidation = (fields, pathNames, length) => {
    const result = {};
    if (!fields) {
        return result;
    }
    pathNames
        .filter(path => fields[path])
        .filter(path => fields[path].length < 0 || fields[path].length > length)
        .forEach(incorrectPath => result[incorrectPath] =
            '* Shouldn\'t be more than ' + length + ' symbols ');
    return result;
}

export const lengthMoreValidation = (fields, pathNames, length) => {
    const result = {};
    if (!fields) {
        return result;
    }
    pathNames
        .filter(path => fields[path])
        .filter(path => fields[path].length < length)
        .forEach(incorrectPath => result[incorrectPath] =
            '* Should be more than ' + length + ' symbols ');
    return result;
}

export const compareFieldsValidation = (fields, fieldName1, fieldName2) => {
    const result = {};
    if (!fields || !fields[fieldName2]) {
        return result;
    }
    if (fields[fieldName1] !== fields[fieldName2]) {
        result[fieldName2] = '* Don\'t match ' + fieldName1 + '.';
    }
    return result;
}

export const validatePhoto = (fields, fieldName) => {
    const result = {};
    if (!fields) {
        return result;
    }
    const photo = get(fields, fieldName);
    console.log(photo)
    if (!photo) {
        return result;
    }

    if (photo.length > 0) {
        const photoContent = photo[0];
        let error;
        if (photoContent.photoUrl) {
            error = validatePhotoUrl(photoContent.photoUrl)
        } else {
            error = validatePhotoFile(photoContent)
        }
        if (error) {
            return {[fieldName]: error}
        }
    }
    return result;
};

export const validatePhotoUrl = (photoURL) => {
    let regexChecker = new RegExp(PATTERN_TO_CHECK_URL);
    if (photoURL && !regexChecker.test(photoURL)) {
        return "* You enter wrong photo url";
    }
};

export const validatePhotoFile = (file) => {
    let regexChecker = new RegExp(PATTERN_TO_CHECK_FILE_EXTENSION);
    if (!regexChecker.test(file.content.type)) {
        return "* You choose file with wrong extension (not png/jpg/jpeg)";
    } /*else if (file.size < 0 || file.size / 1024 / 1024 > 5) {
        setCheckedPhoto(NOT_GOOD_PHOTO_FILE_WITH_WRONG_SIZE);
        setIsPhotoReadyForRegister(false);
    } */
};