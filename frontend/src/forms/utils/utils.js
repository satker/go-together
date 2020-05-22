import {get, set} from "lodash";

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

    const newPhotoObj = {
        id: null,
        photoUrl: null,
        content: {
            type: null,
            photoContent: null
        }
    };

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

export const capitalizeFirstLetter = (str) => str.charAt(0).toLowerCase() + str.slice(1);