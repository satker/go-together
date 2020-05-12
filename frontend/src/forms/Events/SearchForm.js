import React, {useEffect} from 'react';
import PropTypes from "prop-types";
import 'react-dates/initialize';
import {AutosuggestionLocations} from "../utils/components/Autosuggestion";
import 'react-dates/lib/css/_datepicker.css';
import CheckInOutDates from '../utils/components/CheckInOutDates'
import {FORM_DTO, LOCATION_SERVICE_URL, SEARCH_OBJECT_DEFAULT} from '../utils/constants'
import {connect} from "../../App/Context";
import {isEqual} from "lodash";
import {SearchObject} from "../utils/types";
import {setArrivalDate, setDepartureDate, setPage} from "./actions";
import Slider from "@material-ui/core/Slider";
import CustomButton from "../utils/components/CustomButton";

const SearchForm = ({
                        searchObject, onChangeSearchObject, filterObject,
                        setFilterObject, onClearSearchObject,
                        setPage, setArrivalDate, setDepartureDate
                    }) => {

    useEffect(() => {
        const checkDates = (searchObject.arrivalDate && !searchObject.departureDate) ||
            (!searchObject.arrivalDate && searchObject.departureDate);
        if (!isEqual(searchObject, SEARCH_OBJECT_DEFAULT) && !checkDates) {
            const newFilterObject = {...filterObject};
            if (searchObject.advancedSearch.parameters && searchObject.advancedSearch.parameters.length !== 0) {
                newFilterObject.filters["apartment.parameterIds"] = {
                    filterType: "IN",
                    values: searchObject.advancedSearch.parameters
                }
            }
            if (searchObject.advancedSearch.languages && searchObject.advancedSearch.languages.length !== 0) {
                newFilterObject.filters["apartment.ownerLanguages"] = {
                    filterType: "IN",
                    values: searchObject.advancedSearch.languages
                }
            }

            if (searchObject.advancedSearch.rooms !== 0) {
                newFilterObject.filters["apartment.rooms"] = {
                    filterType: "MORE_EQUAL",
                    values: [{
                        id: searchObject.advancedSearch.rooms,
                        name: searchObject.advancedSearch.rooms
                    }]
                }
            }

            if (searchObject.advancedSearch.apartmentTypes && searchObject.advancedSearch.apartmentTypes.length !== 0) {
                newFilterObject.filters["apartment.apartmentType"] = {
                    filterType: "IN",
                    values: searchObject.advancedSearch.apartmentTypes
                }
            }
            if (searchObject.location) {
                newFilterObject.filters["location-service.apartmentLocation.cityId"] = {
                    filterType: "EQUAL",
                    values: [searchObject.location]
                }
            }
            if (searchObject.arrivalDate && searchObject.departureDate) {

                newFilterObject.filters["room-service.room.roomOrderDates"] = {
                    filterType: "NOT_IN_BETWEEN",
                    values: [
                        {
                            id: searchObject.arrivalDate.format('YYYY-MM-DD, hh:mm:ss'),
                            name: searchObject.arrivalDate.format('YYYY-MM-DD, hh:mm:ss')
                        },
                        {
                            id: searchObject.departureDate.format('YYYY-MM-DD, hh:mm:ss'),
                            name: searchObject.departureDate.format('YYYY-MM-DD, hh:mm:ss')
                        }
                    ]
                }
            }

            if (!(searchObject.minCostNight <= 0 && (searchObject.maxCostNight <= 0 || searchObject.maxCostNight === 100))) {
                newFilterObject.filters["room-service.room.costNight"] = {
                    filterType: "BETWEEN",
                    values: [
                        {
                            id: searchObject.minCostNight,
                            name: searchObject.minCostNight
                        },
                        {
                            id: searchObject.maxCostNight,
                            name: searchObject.maxCostNight
                        }
                    ]
                }
            }
            if (searchObject.adult !== 0) {
                newFilterObject.filters["room-service.room.adults"] = {
                    filterType: "MORE_EQUAL",
                    values: [
                        {
                            id: searchObject.adult,
                            name: searchObject.adult
                        }
                    ]
                }
            }
            if (searchObject.children !== 0) {
                newFilterObject.filters["room-service.room.children"] = {
                    filterType: "MORE_EQUAL",
                    values: [
                        {
                            id: searchObject.children,
                            name: searchObject.children
                        }
                    ]
                }
            }

            // TODO: think about recursion call
            /*setState(['arrivalDate', 'departureDate'],
                [searchObject.arrivalDate, searchObject.departureDate]);*/

            newFilterObject.page.page = 0;
            setPage(0);
            setFilterObject(newFilterObject);
        }
    }, [filterObject, searchObject, setFilterObject, setPage]);

    const onAfterChange = (event, newValue) => {
        onChangeSearchObject('maxCostNight', newValue);
    };

    const clearFilters = () => {
        onClearSearchObject();
        setFilterObject({...FORM_DTO("event.id")});
        setArrivalDate(null);
        setDepartureDate(null);
        setPage(0);
    };

    return <div className='container-search-events'>
        <div className='flex'>
            <AutosuggestionLocations formId='search_form_'
                                     setResult={(location) => onChangeSearchObject('location', location)}
                                     placeholder={'Search a location (CITY,COUNTRY)'}
                                     url={LOCATION_SERVICE_URL + '/locations'}
                                     urlParam={'name'}/>
        </div>
        <div className='flex margin-left-custom'>
            <CheckInOutDates startDate={searchObject.arrivalDate}
                             endDate={searchObject.departureDate}
                             setStartDate={(startDate) => onChangeSearchObject('arrivalDate', startDate)}
                             setEndDate={(endDate) => onChangeSearchObject('departureDate', endDate)}/>
        </div>
        <div className='flex margin-left-custom' style={{width: '100px'}}>
            <Slider onChange={onAfterChange}
                    max={300}
                    step={1}
                    value={searchObject.maxCostNight}
                    valueLabelDisplay="on"/>
        </div>
        <div className='flex margin-left-custom'>
            <CustomButton onClick={clearFilters} color="primary" text='Clear'/>
        </div>
    </div>
};

SearchForm.propTypes = {
    searchObject: SearchObject.isRequired,
    onChangeSearchObject: PropTypes.func.isRequired,
    onClearSearchObject: PropTypes.func.isRequired,
    setFilterObject: PropTypes.func.isRequired,
    setPage: PropTypes.func.isRequired,
    setArrivalDate: PropTypes.func.isRequired,
    setDepartureDate: PropTypes.func.isRequired
};

export default connect(null,
    {setPage, setArrivalDate, setDepartureDate})(SearchForm);