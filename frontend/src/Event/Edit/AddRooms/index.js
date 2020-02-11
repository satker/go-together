import React, {useState} from 'react';
import {Nav, NavItem, NavLink, TabContent, TabPane} from "reactstrap";
import classnames from "classnames";
import LifeRooms from "./LifeRooms";
import OtherRooms from "./OtherRooms";
import PropTypes from "prop-types";
import {Room} from "../../../types";

const AddRooms = ({lifeRooms, otherRooms, onChange}) => {
    const [activeTab, setActiveTab] = useState('1');

    const toggle = (tab) => {
        if (activeTab !== tab) {
            setActiveTab(tab);
        }
    };

    return <div className='flex'>
        <Nav tabs>
            <NavItem>
                <NavLink
                    className={classnames({active: activeTab === '1'})}
                    onClick={() => toggle('1')}>
                    Guest rooms
                </NavLink>
            </NavItem>
            <NavItem>
                <NavLink
                    className={classnames({active: activeTab === '2'})}
                    onClick={() => toggle('2')}>
                    Rooms for your comfort
                </NavLink>
            </NavItem>
        </Nav>
        <TabContent activeTab={activeTab}>
            <TabPane tabId="1">
                <LifeRooms lifeRooms={lifeRooms}
                           onChange={onChange(true)}/>
            </TabPane>
            <TabPane tabId="2">
                <OtherRooms otherRooms={otherRooms}
                            onChange={onChange(false)}/>
            </TabPane>
        </TabContent>
    </div>
};

AddRooms.propTypes = {
    lifeRooms: PropTypes.arrayOf(Room).isRequired,
    otherRooms: PropTypes.arrayOf(Room).isRequired,
    onChange: PropTypes.func.isRequired
};

export default AddRooms;