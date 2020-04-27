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
    GOOD_PASSWORD,
    GOOD_PHOTO,
    NOT_GOOD_CONFIRM_PASSWORD,
    NOT_GOOD_DESCRIPTION,
    NOT_GOOD_FIRST_NAME,
    NOT_GOOD_LAST_NAME,
    NOT_GOOD_PASSWORD,
    NOT_GOOD_PHOTO_FILE_EXT,
    NOT_GOOD_PHOTO_URL,
    PATTERN_TO_CHECK_FILE_EXTENSION,
    PATTERN_TO_CHECK_MAIL,
    PATTERN_TO_CHECK_NAME,
    PATTERN_TO_CHECK_URL
} from "./constants";


export const handleUserName = (value, setCheckedUserName, setIsUserNameReadyForRegister, getCheckUserName) => {
    if (value === '') {
        setCheckedUserName(EMPTY_LOGIN);
        setIsUserNameReadyForRegister(false);
        return;
    }

    getCheckUserName(value)
};

export const handleMail = (value, setCheckedMail, setIsMailReadyForRegister, getCheckMail) => {
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

    getCheckMail(value);
};

export const handleName = (value, type, setCheckedFirstName, setIsFirstNameReadyForRegister, setCheckedLastName,
                           setIsLastNameReadyForRegister) => {
    let regexToCheckNonNumericValue = new RegExp(PATTERN_TO_CHECK_NAME);
    let isGood = regexToCheckNonNumericValue.test(value);
    if (type === 'firstName') {
        if (isGood) {
            if (value === '') {
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
            if (value === '') {
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

export const handlePassword = (value, setCheckedPassword, setIsPasswordReadyForRegister) => {
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

export const handleConfirmPassword = (value, setCheckedConfirmPassword, setIsConfirmPasswordReadyForRegister, password) => {
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

export const handleDescription = (value, setCheckedDescription, setIsDescriptionReadyForRegister) => {
    if (value.length > 255) {
        setCheckedDescription(NOT_GOOD_DESCRIPTION);
        setIsDescriptionReadyForRegister(false);
    } else {
        setCheckedDescription(GOOD_DESCRIPTION);
        setIsDescriptionReadyForRegister(true);
    }
};

export const handlePhoto = (photo, setCheckedPhoto, setIsPhotoReadyForRegister) => {
    if (photo.photoUrl) {
        handleUrlPhoto(photo.photoUrl, setCheckedPhoto, setIsPhotoReadyForRegister);
    } else {
        onFileChangeHandler(photo, setCheckedPhoto, setIsPhotoReadyForRegister)
    }
};

export const handleUrlPhoto = (photoURL, setCheckedPhoto, setIsPhotoReadyForRegister) => {
    let regexChecker = new RegExp(PATTERN_TO_CHECK_URL);
    if (photoURL && !regexChecker.test(photoURL)) {
        setCheckedPhoto(NOT_GOOD_PHOTO_URL);
        setIsPhotoReadyForRegister(false);
    } else {
        setCheckedPhoto(GOOD_PHOTO);
        setIsPhotoReadyForRegister(true);
    }
};

export const onFileChangeHandler = (file, setCheckedPhoto, setIsPhotoReadyForRegister) => {
    let regexChecker = new RegExp(PATTERN_TO_CHECK_FILE_EXTENSION);
    if (!regexChecker.test(file.content.type)) {
        setCheckedPhoto(NOT_GOOD_PHOTO_FILE_EXT);
        setIsPhotoReadyForRegister(false);
    } /*else if (file.size < 0 || file.size / 1024 / 1024 > 5) {
        setCheckedPhoto(NOT_GOOD_PHOTO_FILE_WITH_WRONG_SIZE);
        setIsPhotoReadyForRegister(false);
    } */ else {
        setCheckedPhoto(GOOD_PHOTO);
        setIsPhotoReadyForRegister(true);
    }
};