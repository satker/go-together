import {get, keys, set} from "lodash";
import moment from "moment";

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

export const getCorrectDateFromString = (date) => moment(date).format('LLL');

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

export const FilterOperator = {
    LIKE: {
        operator: "LIKE"
    },
    EQUAL: {
        operator: "EQUAL"
    },
    IN: {
        operator: 'IN'
    },
    START_DATE: {
        operator: 'START_DATE'
    },
    END_DATE: {
        operator: 'END_DATE'
    },
    NEAR_LOCATION: {
        operator: 'NEAR_LOCATION'
    }
}

export const getFilterDto = (filterType, values) => {
    return {
        filterType: filterType.operator,
        values: values
    }
}

export const updateFormDto = (currentFilter, filterType, values, searchField,
                              havingCount) => {
    let resultFilterObject = {...currentFilter};
    if (havingCount) {
        console.log(havingCount, keys(resultFilterObject.filters),
            searchField
        )
        const findValue = keys(resultFilterObject.filters)
            .find(keyFilter => keyFilter.startsWith(searchField));
        if (findValue) {
            delete resultFilterObject.filters[findValue];
        }
        searchField = searchField + ':' + havingCount;
    }
    if (values) {
        resultFilterObject.filters[searchField] = getFilterDto(filterType, values);
    } else if (resultFilterObject.filters.hasOwnProperty(searchField)) {
        delete resultFilterObject.filters[searchField];
    } else {
        return;
    }
    return resultFilterObject;
}