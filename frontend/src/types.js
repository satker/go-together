import * as PropTypes from "prop-types";

export const Location = PropTypes.exact({
    id: PropTypes.string,
    name: PropTypes.string,
    state: PropTypes.string,
    country: PropTypes.exact({
        id: PropTypes.string,
        countryCode: PropTypes.string,
        name: PropTypes.string
    })
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

export const Language = PropTypes.exact({
    id: PropTypes.string.isRequired,
    name: PropTypes.string.isRequired,
    code: PropTypes.string.isRequired
});

export const Interest = PropTypes.exact({
    id: PropTypes.string.isRequired,
    name: PropTypes.string.isRequired,
});

export const User = PropTypes.exact({
    id: PropTypes.string,
    login: PropTypes.string,
    mail: PropTypes.string,
    firstName: PropTypes.string,
    lastName: PropTypes.string,
    location: Location,
    description: PropTypes.string,
    password: PropTypes.string,
    role: PropTypes.string,
    userPhoto: PropTypes.arrayOf(PhotoObject),
    languages: PropTypes.arrayOf(Language),
    interests: PropTypes.arrayOf(Interest)
});

export const EventPhoto = PropTypes.exact({
    id: PropTypes.string,
    photos: PropTypes.arrayOf(PhotoObject)
});

export const PaidThing = PropTypes.exact({
    id: PropTypes.string.isRequired,
    name: PropTypes.string.isRequired
});

export const CashPaidThing = PropTypes.exact({
    id: PropTypes.string,
    cashCategory: PropTypes.string.isRequired,
    paidThing: PaidThing
});

export const Route = PropTypes.exact({
    id: PropTypes.string,
    routeNumber: PropTypes.number.isRequired,
    address: PropTypes.string,
    latitude: PropTypes.number,
    longitude: PropTypes.number,
    location: Location
});

export const Event = PropTypes.exact({
    id: PropTypes.string,
    name: PropTypes.string.isRequired,
    author: User,
    peopleCount: PropTypes.number.isRequired,
    housingType: PropTypes.string.isRequired,
    description: PropTypes.string.isRequired,
    eventPhotoDto: EventPhoto,
    paidThings: PropTypes.arrayOf(CashPaidThing),
    users: PropTypes.arrayOf(User),
    peopleLike: PropTypes.number,
    route: PropTypes.arrayOf(Route)
});

export const Review = PropTypes.exact({
    rating: PropTypes.number,
    dateCreation: PropTypes.string,
    message: PropTypes.string,
    user: User
});

export const SearchObject = PropTypes.exact({
    location: SimpleObject,
    startDate: PropTypes.object,
    endDate: PropTypes.object,
    advancedSearch: {
        beds: PropTypes.number,
        rooms: PropTypes.number,
        eventTypes: PropTypes.array,
        parameters: PropTypes.array,
        languages: PropTypes.array
    },
    page: PropTypes.number
});
