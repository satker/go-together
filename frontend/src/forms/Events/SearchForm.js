import React, {useEffect, useState} from 'react';
import {Button, Dropdown, DropdownItem, DropdownMenu, DropdownToggle} from "reactstrap";
import PropTypes from "prop-types";
import {Range} from 'rc-slider';
import 'rc-slider/assets/index.css';
import 'react-dates/initialize';
import createAutosuggestion from "../utils/components/Autosuggestion";
import 'react-dates/lib/css/_datepicker.css';
import CheckInOutDates from '../utils/components/CheckInOutDates'
import CounterItem from "../utils/components/CounterItem";
import AdvancedSearch from "./AdvancedSearch";
import {FORM_DTO, LOCATION_SERVICE_URL, SEARCH_OBJECT_DEFAULT} from '../utils/constants'
import {connect} from "../../App/Context";
import {isEqual} from "lodash";
import {SearchObject} from "../utils/types";
import {FORM_ID} from "./constants";
import {setArrivalDate, setDepartureDate, setPage} from "./actions";

const Autosuggestion = createAutosuggestion('AutosuggestionLocation');

const SearchForm = ({
                        searchObject, onChangeSearchObject, filterObject,
                        setFilterObject, onClearSearchObject,
                        setPage, setArrivalDate, setDepartureDate
                    }) => {
    const [dropdownPriceOpen, setDropdownPriceOpen] = useState(false);
    const [dropdownCapacityOpen, setDropdownCapacityOpen] = useState(false);

    const [focus, setFocus] = useState(false);

    useEffect(() => {
        const checkDates = (searchObject.arrivalDate && !searchObject.departureDate) ||
            (!searchObject.arrivalDate && searchObject.departureDate);
        if (!focus && !isEqual(searchObject, SEARCH_OBJECT_DEFAULT) && !checkDates) {
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
    }, [focus, searchObject, setFilterObject, setPage]);

    const onAfterChange = (value) => {
        onChangeSearchObject('minCostNight', value[0]);
        onChangeSearchObject('maxCostNight', value[1]);
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
            <Autosuggestion formId='search_form_'
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
        <div className='flex margin-left-custom'>
            <Dropdown size={'sm'}
                      isOpen={dropdownPriceOpen} toggle={() => {
                if (!focus) {
                    setDropdownPriceOpen(!dropdownPriceOpen);
                    setFocus(!focus);
                }
            }}>
                <DropdownToggle caret>
                    Price
                </DropdownToggle>
                <DropdownMenu right>
                    <DropdownItem header>Choose price range:</DropdownItem>
                    <DropdownItem><Range onChange={onAfterChange} max={300}
                                         defaultValue={[searchObject.minCostNight, searchObject.maxCostNight]}/></DropdownItem>
                    <DropdownItem
                        disabled>{searchObject.minCostNight + '$ - ' + searchObject.maxCostNight + '$'}</DropdownItem>
                    <Button onClick={() => {
                        setDropdownPriceOpen(!dropdownPriceOpen);
                        setFocus(!focus)
                    }} color="primary">Ok</Button>
                </DropdownMenu>
            </Dropdown>
        </div>
        <div className='flex margin-left-custom'>
            <Dropdown size={'sm'} isOpen={dropdownCapacityOpen} toggle={() => {
                if (!focus) {
                    setDropdownCapacityOpen(!dropdownCapacityOpen);
                    setFocus(!focus);
                }
            }}>
                <DropdownToggle caret>
                    Guests
                </DropdownToggle>
                <DropdownMenu right>
                    <DropdownItem header>Choose capacity:</DropdownItem>
                    Adults: <CounterItem value={searchObject.adult}
                                         setValue={value => onChangeSearchObject('adult', value)}/>
                    Children: <CounterItem value={searchObject.children}
                                           setValue={value => onChangeSearchObject('children', value)}/>
                    <Button onClick={() => {
                        setDropdownCapacityOpen(!dropdownCapacityOpen);
                        setFocus(!focus)
                    }} color="primary">Ok</Button>
                </DropdownMenu>
            </Dropdown>
        </div>
        <div className='flex margin-left-custom'>
            <AdvancedSearch focus={focus}
                            setFocus={setFocus}
                            searchObject={searchObject}
                            onChangeSearchObject={onChangeSearchObject}
            />
        </div>
        <div className='flex margin-left-custom'>
            <Button onClick={clearFilters} color="primary">Clear</Button>
        </div>
    </div>
};

SearchForm.propTypes = {
    searchObject: SearchObject.isRequired,
    onChangeSearchObject: PropTypes.func.isRequired,
    onClearSearchObject: PropTypes.func.isRequired,
    setFilterObject: PropTypes.object,
    setPage: PropTypes.func.isRequired,
    setArrivalDate: PropTypes.func.isRequired,
    setDepartureDate: PropTypes.func.isRequired
};

export default connect(null,
    {setPage, setArrivalDate, setDepartureDate})(SearchForm)(FORM_ID);