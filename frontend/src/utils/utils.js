import {get, set} from "lodash";
import {PHOTO_OBJECT} from "./constants";

export const getSrcForImg = (photoObj) => {
    if (!photoObj) {
        return 'https://sisterhoodofstyle.com/wp-content/uploads/2018/02/no-image-1.jpg';
    } else if (photoObj.photoUrl) {
        return photoObj.photoUrl
    } else if (photoObj.content && photoObj.content.type) {
        return photoObj.content.type + photoObj.content.photoContent;
    } else {
        return 'https://sisterhoodofstyle.com/wp-content/uploads/2018/02/no-image-1.jpg';
    }
};

export const createFileReaderToParsePhoto = (photo) => new Promise((resolve) => {
    const fileReader = new FileReader();
    fileReader.onloadend = () => resolve(fileReader.result);
    fileReader.readAsDataURL(photo);
});

export const createPhotoObj = (isUrl, data) => {

    const newPhotoObj = {...PHOTO_OBJECT}

    if (isUrl) {
        newPhotoObj.photoUrl = data;
        return newPhotoObj;
    } else {
        const parsed = data.split(',');
        newPhotoObj.content.type = parsed[0] + ',';
        newPhotoObj.content.photoContent = parsed[1];
        return newPhotoObj;
    }
};

export const s4 = () => Math.floor((1 + Math.random()) * 0x10000).toString(16).substring(1);

export const getRandomNum = () => s4() + s4();

export const onChange = (state, setState) => (path, value) => {
    if (path instanceof Array && value instanceof Array) {
        if (path.length === value.length) {
            const newState = {...state};
            for (let i = 0; i < path.length; i++) {
                if (get(state, path[i]) !== value[i]) {
                    set(newState, path[i], value[i]);
                }
            }
            setState(newState);
        }
    } else {
        if (get(state, path) !== value) {
            const newState = {...state};
            set(newState, path, value);
            setState(newState);
        }
    }
};