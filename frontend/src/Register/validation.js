import {createFileReaderToParsePhoto, createPhotoObj} from "../utils/utils";
import {
    EMPTY_FIRST_NAME,
    EMPTY_LAST_NAME,
    EMPTY_LOGIN,
    EMPTY_MAIL,
    EMPTY_PASSWORD,
    GOOD_CONFIRM_PASSWORD,
    GOOD_DESCRIPTION,
    GOOD_FIRST_NAME,
    GOOD_LAST_NAME,
    GOOD_LOGIN,
    GOOD_MAIL,
    GOOD_PASSWORD,
    GOOD_PHOTO,
    NOT_GOOD_CONFIRM_PASSWORD,
    NOT_GOOD_DESCRIPTION,
    NOT_GOOD_FIRST_NAME,
    NOT_GOOD_LAST_NAME,
    NOT_GOOD_LOGIN,
    NOT_GOOD_MAIL,
    NOT_GOOD_PASSWORD,
    NOT_GOOD_PHOTO_FILE_EXT,
    NOT_GOOD_PHOTO_FILE_WITH_WRONG_SIZE,
    NOT_GOOD_PHOTO_URL,
    PATTERN_TO_CHECK_FILE_EXTENSION,
    PATTERN_TO_CHECK_MAIL,
    PATTERN_TO_CHECK_NAME,
    PATTERN_TO_CHECK_URL
} from "./constants";

import {USER_SERVICE_URL} from "../utils/constants";

export const onFileChangeHandler = (setCheckedPhoto, setIsPhotoReadyForRegister, setUserPhoto) => evt => {
    const file = evt.target.files.item(0);
    let regexChecker = new RegExp(PATTERN_TO_CHECK_FILE_EXTENSION);
    if (!regexChecker.test(file.type)) {
        setCheckedPhoto(NOT_GOOD_PHOTO_FILE_EXT);
        setIsPhotoReadyForRegister(false);
    } else if (file.size < 0 || file.size / 1024 / 1024 > 5) {
        setCheckedPhoto(NOT_GOOD_PHOTO_FILE_WITH_WRONG_SIZE);
        setIsPhotoReadyForRegister(false);
    } else {
        setCheckedPhoto(GOOD_PHOTO);
        setIsPhotoReadyForRegister(true);
        createFileReaderToParsePhoto(file, (parsedPhoto) =>
            setUserPhoto(createPhotoObj(false, parsedPhoto)))
    }
};

export const handleUserName = (evt, setCheckedUserName, setIsUserNameReadyForRegister, fetchWithToken) => {
    let value = evt.target.value;
    if (value === '') {
        setCheckedUserName(EMPTY_LOGIN);
        setIsUserNameReadyForRegister(false);
        return;
    }
    const URLtoCheck = USER_SERVICE_URL + '/users/check/login/' + value;

    const responseHandler = response => {
        if (response === true) {
            setCheckedUserName(NOT_GOOD_LOGIN);
            setIsUserNameReadyForRegister(false);
        } else {
            setCheckedUserName(GOOD_LOGIN);
            setIsUserNameReadyForRegister(true);
        }
    };

    fetchWithToken(URLtoCheck, responseHandler)
};

export const handleMail = (evt, setCheckedMail, setIsMailReadyForRegister, fetchWithToken) => {
    let value = evt.target.value;
    if (value === '') {
        setCheckedMail(EMPTY_MAIL);
        setIsMailReadyForRegister(false);
        return;
    }
    let regexChecker = new RegExp(PATTERN_TO_CHECK_MAIL);
    if (!regexChecker.test(value)) {
        setCheckedMail(EMPTY_MAIL);
        setIsMailReadyForRegister(false);
        return;
    }
    const URLtoCheck = USER_SERVICE_URL + '/users/check/mail/' + value;

    const responseHandler = response => {
        if (response === true) {
            setCheckedMail(NOT_GOOD_MAIL);
            setIsMailReadyForRegister(false);
        } else {
            setCheckedMail(GOOD_MAIL);
            setIsMailReadyForRegister(true);
        }
    };

    fetchWithToken(URLtoCheck, responseHandler)
};

export const handleName = (evt, setCheckedFirstName, setIsFirstNameReadyForRegister, setCheckedLastName,
                           setIsLastNameReadyForRegister) => {
    let value = evt.target.value;
    let regexToCheckNonNumericValue = new RegExp(PATTERN_TO_CHECK_NAME);
    let isGood = regexToCheckNonNumericValue.test(value);
    if (evt.target.name === 'firstName') {
        if (isGood) {
            if (evt.target.value === '') {
                setCheckedFirstName(EMPTY_FIRST_NAME);
                setIsFirstNameReadyForRegister(false);
            } else {
                setCheckedFirstName(GOOD_FIRST_NAME);
                setIsFirstNameReadyForRegister(true);
            }
        } else {
            setCheckedFirstName(NOT_GOOD_FIRST_NAME);
            setIsFirstNameReadyForRegister(false);
        }
    } else {
        if (isGood) {
            if (evt.target.value === '') {
                setCheckedLastName(EMPTY_LAST_NAME);
                setIsLastNameReadyForRegister(false);
            } else {
                setCheckedLastName(GOOD_LAST_NAME);
                setIsLastNameReadyForRegister(true);
            }
        } else {
            setCheckedLastName(NOT_GOOD_LAST_NAME);
            setIsLastNameReadyForRegister(false);
        }
    }
};

export const handlePassword = (evt, setCheckedPassword, setIsPasswordReadyForRegister) => {
    let value = evt.target.value;
    if (value === '') {
        setCheckedPassword(EMPTY_PASSWORD);
        setIsPasswordReadyForRegister(false);
    }
    if (value.length > 7) {
        setCheckedPassword(GOOD_PASSWORD);
        setIsPasswordReadyForRegister(true);
    } else {
        setCheckedPassword(NOT_GOOD_PASSWORD);
        setIsPasswordReadyForRegister(false);
    }
};

export const handleConfirmPassword = (evt, setCheckedConfirmPassword, setIsConfirmPasswordReadyForRegister, password) => {
    let value = evt.target.value;
    if (value === '') {
        setCheckedConfirmPassword(EMPTY_PASSWORD);
        setIsConfirmPasswordReadyForRegister(false);
    }
    if (value === password) {
        setCheckedConfirmPassword(GOOD_CONFIRM_PASSWORD);
        setIsConfirmPasswordReadyForRegister(true);
    } else {
        setCheckedConfirmPassword(NOT_GOOD_CONFIRM_PASSWORD);
        setIsConfirmPasswordReadyForRegister(false);
    }
};

export const handleDescription = (evt, setCheckedDescription, setIsDescriptionReadyForRegister) => {
    let value = evt.target.value;
    if (value.length > 255) {
        setCheckedDescription(NOT_GOOD_DESCRIPTION);
        setIsDescriptionReadyForRegister(false);
    } else {
        setCheckedDescription(GOOD_DESCRIPTION);
        setIsDescriptionReadyForRegister(true);
    }
};

export const handlePhoto = (evt, setCheckedPhoto, setIsPhotoReadyForRegister) => {
    const photoURL = evt.target.value;
    let regexChecker = new RegExp(PATTERN_TO_CHECK_URL);
    if (photoURL && !regexChecker.test(photoURL)) {
        setCheckedPhoto(NOT_GOOD_PHOTO_URL);
        setIsPhotoReadyForRegister(false);
    } else {
        setCheckedPhoto(GOOD_PHOTO);
        setIsPhotoReadyForRegister(true);
    }
};
