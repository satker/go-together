import React, {useContext, useEffect, useState} from 'react';
import {Button, Dropdown, DropdownItem, DropdownMenu, DropdownToggle} from "reactstrap";
import CounterItem from "../utils/components/CounterItem";
import {EVENT_SERVICE_URL, USER_SERVICE_URL} from '../utils/constants'
import {Context} from "../Context";
import * as PropTypes from "prop-types";
import {SearchObject} from "../types";
import MultipleSelectBox from "../utils/components/MultipleSelectBox";

const AdvancedSearch = ({focus, setFocus, searchObject, onChangeSearchObject}) => {
    const [state] = useContext(Context);

    const [dropdownAdvancedSearchOpen, setDropdownAdvancedSearchOpen] = useState(false);

    const [apartmentTypes, setApartmentTypes] = useState([]);
    const [parameters, setParameters] = useState([]);
    const [languages, setLanguages] = useState([]);

    const [rooms, setRooms] = useState(searchObject.advancedSearch.rooms);
    const [chooseParameters, setChooseParameters] = useState([]);
    const [chooseLanguages, setChooseLanguages] = useState([]);
    const [chooseApartmentTypes, setChooseApartmentTypes] = useState([]);

    useEffect(() => {
        setChooseParameters(searchObject.advancedSearch.parameters
            .map(param => ({value: param.id, label: param.name})));
        setChooseLanguages(searchObject.advancedSearch.languages
            .map(param => ({value: param.id, label: param.name})));
        setChooseApartmentTypes(searchObject.advancedSearch.apartmentTypes
            .map(param => ({value: param.id, label: param.name})));
    }, [searchObject]);

    useEffect(() => {
        if (dropdownAdvancedSearchOpen) {
            state.fetchWithToken(EVENT_SERVICE_URL + '/parameters', setParameters);
            state.fetchWithToken(USER_SERVICE_URL + '/languages', setLanguages);
            state.fetchWithToken(EVENT_SERVICE_URL + '/types', setApartmentTypes);
        }
    }, [dropdownAdvancedSearchOpen, state]);

    const onAction = () => {
        const advancedSearch = {
            rooms,
            apartmentTypes: chooseApartmentTypes.map(param => ({id: param.value, name: param.label})),
            parameters: chooseParameters.map(param => ({id: param.value, name: param.label})),
            languages: chooseLanguages.map(param => ({id: param.value, name: param.label}))
        };
        setChooseParameters([]);
        setApartmentTypes([]);
        setLanguages([]);
        onChangeSearchObject('advancedSearch', advancedSearch)
    };


    return <Dropdown size={'sm'} isOpen={dropdownAdvancedSearchOpen} toggle={() => {
        if (!focus) {
            setDropdownAdvancedSearchOpen(!dropdownAdvancedSearchOpen);
            setFocus(!focus);
        }
    }}>
        <DropdownToggle caret>
            Advanced search
        </DropdownToggle>
        <DropdownMenu right>
            <DropdownItem header>Choose options:</DropdownItem>
            Rooms: <CounterItem value={rooms} setValue={setRooms}/>
            <DropdownItem divider/>
            <DropdownItem toggle={false}>
                <MultipleSelectBox label='Apartment types:'
                                   onChange={evt => setChooseApartmentTypes(evt)}
                                   optionsSimple={apartmentTypes}
                                   value={chooseApartmentTypes}/>
            </DropdownItem>
            <DropdownItem divider/>
            <DropdownItem>
                <MultipleSelectBox label='Owner language:'
                                   value={chooseLanguages}
                                   optionsSimple={languages}
                                   onChange={evt => setChooseLanguages(evt)}/>
            </DropdownItem>
            <DropdownItem divider/>
            <DropdownItem>
                <MultipleSelectBox label='Parameters:'
                                   value={chooseParameters}
                                   optionsSimple={parameters}
                                   onChange={evt => setChooseParameters(evt)}/>
            </DropdownItem>
            <Button onClick={() => {
                setDropdownAdvancedSearchOpen(!dropdownAdvancedSearchOpen);
                onAction();
                setFocus(!focus)
            }} color="primary">Ok</Button>
        </DropdownMenu>
    </Dropdown>
};

AdvancedSearch.propTypes = {
    focus: PropTypes.bool.isRequired,
    setFocus: PropTypes.func.isRequired,
    onChangeSearchObject: PropTypes.func.isRequired,
    searchObject: SearchObject.isRequired
};

export default AdvancedSearch;