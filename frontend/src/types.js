import * as PropTypes from "prop-types";

export const City = PropTypes.exact({
    id: PropTypes.string,
    name: PropTypes.string,
    state: PropTypes.string,
    country: PropTypes.exact({
        id: PropTypes.string,
        countryCode: PropTypes.string,
        name: PropTypes.string
    })
});

export const Location = PropTypes.exact({
    city: City,
    address: PropTypes.string,
    latitude: PropTypes.number,
    longitude: PropTypes.number,
    apartmentId: PropTypes.string
});

export const SimpleObject = PropTypes.exact({
    id: PropTypes.string,
    name: PropTypes.string
});

export const PhotoObject = PropTypes.exact({
    id: PropTypes.string,
    photoUrl: PropTypes.string,
    content: PropTypes.exact({
        type: PropTypes.string,
        photoContent: PropTypes.string
    })
});

export const User = PropTypes.exact({
    id: PropTypes.string,
    login: PropTypes.string,
    mail: PropTypes.string,
    firstName: PropTypes.string,
    lastName: PropTypes.string,
    location: City,
    description: PropTypes.string,
    password: PropTypes.string,
    role: PropTypes.string,
    userPhoto: PhotoObject,
    languages: PropTypes.arrayOf(PropTypes.string)
});

export const SearchApartment = PropTypes.exact({
    id: PropTypes.string,
    apartmentName: PropTypes.string,
    apartmentType: SimpleObject,
    mainPhoto: PhotoObject,
    location: Location,

    rating: PropTypes.number,
    peopleLiked: PropTypes.number,
    price: PropTypes.number,
    photos: PropTypes.arrayOf(PhotoObject),
    roomCount: PropTypes.number
});

export const CreateApartment = PropTypes.exact({
    id: PropTypes.string,
    apartmentName: PropTypes.string,
    apartmentType: SimpleObject,
    mainPhoto: PhotoObject,
    location: Location,

    description: PropTypes.string,
    parameters: PropTypes.arrayOf(SimpleObject),

    userId: PropTypes.string,
    rooms: PropTypes.object
});

export const ApartmentView = PropTypes.exact({
    id: PropTypes.string,
    apartmentName: PropTypes.string,
    apartmentType: SimpleObject,
    mainPhoto: PhotoObject,
    location: Location,

    description: PropTypes.string,
    parameters: PropTypes.arrayOf(SimpleObject),

    rating: PropTypes.number,
    price: PropTypes.number,
    photos: PropTypes.arrayOf(PhotoObject),
    roomCount: PropTypes.number,
    ownerId: PropTypes.string.isRequired
});

export const Capacity = PropTypes.exact({
    adults: PropTypes.number,
    children: PropTypes.number
});

export const Bed = PropTypes.exact({
    id: PropTypes.string,
    bedType: PropTypes.exact({
        id: PropTypes.string,
        description: PropTypes.string,
        photo: PhotoObject,
        capacity: Capacity
    }),
    icon: PhotoObject,
    count: PropTypes.number
});

export const Room = PropTypes.exact({
    id: PropTypes.string,
    isSnoozed: PropTypes.bool,
    roomType: PropTypes.string,
    roomSize: PropTypes.number,
    photos: PropTypes.arrayOf(PhotoObject),
    description: PropTypes.string,
    costNight: PropTypes.number,
    beds: PropTypes.arrayOf(Bed)
});

export const Review = PropTypes.exact({
    rating: PropTypes.number,
    dateCreation: PropTypes.string,
    message: PropTypes.string,
    user: User
});

export const SearchObject = PropTypes.exact({
    minCostNight: PropTypes.number,
    maxCostNight: PropTypes.number,
    location: SimpleObject,
    arrivalDate: PropTypes.object,
    departureDate: PropTypes.object,
    adult: PropTypes.number,
    children: PropTypes.number,
    advancedSearch: {
        beds: PropTypes.number,
        rooms: PropTypes.number,
        apartmentTypes: PropTypes.array,
        parameters: PropTypes.array,
        languages: PropTypes.array
    },
    page: PropTypes.number
});
