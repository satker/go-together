import React, {useEffect, useState} from 'react';
import {Button, Dropdown, DropdownItem, DropdownMenu, DropdownToggle} from "reactstrap";
import CounterItem from "../utils/components/CounterItem";
import {connect} from "../../App/Context";
import * as PropTypes from "prop-types";
import {SearchObject} from "../utils/types";
import MultipleSelectBox from "../utils/components/MultipleSelectBox";
import {FORM_ID} from "./constants";
import {getApartmentTypes, getLanguages, getParameters} from "./actions";

const AdvancedSearch = ({
                            focus, setFocus, searchObject, onChangeSearchObject,
                            getParameters, getLanguages, getApartmentTypes,
                            parameters, languages, apartmentTypes
                        }) => {
    const [dropdownAdvancedSearchOpen, setDropdownAdvancedSearchOpen] = useState(false);

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
            getParameters();
            getLanguages();
            getApartmentTypes();
        }
    }, [dropdownAdvancedSearchOpen, getLanguages, getApartmentTypes, getParameters]);

    const onAction = () => {
        const advancedSearch = {
            rooms,
            apartmentTypes: chooseApartmentTypes.map(param => ({id: param.value, name: param.label})),
            parameters: chooseParameters.map(param => ({id: param.value, name: param.label})),
            languages: chooseLanguages.map(param => ({id: param.value, name: param.label}))
        };
        setChooseParameters([]);
        setChooseApartmentTypes([]);
        setChooseLanguages([]);
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
    searchObject: SearchObject.isRequired,
    getParameters: PropTypes.func.isRequired,
    getLanguages: PropTypes.func.isRequired,
    getApartmentTypes: PropTypes.func.isRequired,
    parameters: PropTypes.array,
    apartmentTypes: PropTypes.array,
    languages: PropTypes.array
};

const mapStateToProps = state => ({
    parameters: state[FORM_ID]?.parameters || [],
    apartmentTypes: state[FORM_ID]?.apartmentTypes || [],
    languages: state[FORM_ID]?.languages || []
});

export default connect(mapStateToProps,
    {getParameters, getLanguages, getApartmentTypes},
    FORM_ID)(AdvancedSearch);