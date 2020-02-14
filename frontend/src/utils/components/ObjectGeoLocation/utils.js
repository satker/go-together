export const getAddress = (response) => {
    //const address = response.results[0].formatted_address;
    //onChange('address', address);
    return response.results[0].formatted_address;
};

export const getCity = (response) => {
    const addressArray = response.results[0].address_components;
    let city = '';
    for (let i = 0; i < addressArray.length; i++) {
        if (addressArray[i].types[0] && 'administrative_area_level_2' === addressArray[i].types[0]) {
            city = addressArray[i].long_name;
            //onChange('location.name', city);
            return city;
        }
    }
};

export const getCountry = (response) => {
    const addressArray = response.results[0].address_components;
    let country = '';
    for (let i = 0; i < addressArray.length; i++) {
        for (let i = 0; i < addressArray.length; i++) {
            if (addressArray[i].types[0] && 'country' === addressArray[i].types[0]) {
                country = addressArray[i].long_name;
                //onChange('location.country.name', country);
                return country;
            }
        }
    }
};

export const getState = (response) => {
    const addressArray = response.results[0].address_components;
    let state = '';
    for (let i = 0; i < addressArray.length; i++) {
        for (let i = 0; i < addressArray.length; i++) {
            if (addressArray[i].types[0] && 'administrative_area_level_1' === addressArray[i].types[0]) {
                state = addressArray[i].long_name;
                //onChange('location.state', state);
                return state;
            }
        }
    }
};