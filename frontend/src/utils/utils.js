export const getSrcForImg = (photoObj) => {
    if (photoObj.photoUrl) {
        return photoObj.photoUrl
    } else if (photoObj.content && photoObj.content.type) {
        return photoObj.content.type + photoObj.content.photoContent;
    } else {
        return 'https://sisterhoodofstyle.com/wp-content/uploads/2018/02/no-image-1.jpg';
    }
};

export const createFileReaderToParsePhoto = (photo, func) => {
    const fr = new FileReader();
    fr.onload = function (event) {
        func(event.target.result);
    };
    fr.readAsDataURL(photo);
};

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

export const s4 = () => Math.floor((1 + Math.random()) * 0x10000).toString(16).substring(1);

export const getRandomNum = () => s4() + s4();