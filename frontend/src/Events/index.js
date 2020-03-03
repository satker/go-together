import React, {useContext, useEffect, useState} from "react";
import SearchForm from "./SearchForm";
import {Context} from "../Context";
import {Container} from "reactstrap";
import ReactPaginate from 'react-paginate';
import {EVENTS_URL, FORM_DTO, SEARCH_OBJECT_DEFAULT} from '../utils/constants'
import './styles.css'
import {get, isEqual, set} from "lodash";
import GroupItems from "../utils/components/CardItems";

const EVENT_PAGE_FIND_URL = EVENTS_URL + '/find';

const Events = () => {
    const [events, setEvents] = useState([]);
    const [pageCount, setPageCount] = useState(0);
    const [searchObject, setSearchObject] = useState({...SEARCH_OBJECT_DEFAULT});
    const [filterObject, setFilterObject] = useState({...FORM_DTO("apartment.id")});

    const [state, setState] = useContext(Context);

    useEffect(() => {
        filterObject.page.size = state.pageSize;
        state.fetchWithToken(EVENT_PAGE_FIND_URL, response => {
            const newEvents = {
                events: response.result,
                page: state.page

            };
            setEvents(newEvents);
            setPageCount(response.page.totalSize / response.page.size);
        }, 'POST', filterObject);
    }, [state, filterObject]);

    const onClickChooseEvent = (event) => setState('eventId', event.id);

    const onClickNextPage = page => {
        filterObject.page.size = state.pageSize;
        filterObject.page.page = page.selected;
        state.fetchWithToken(EVENT_PAGE_FIND_URL, response => {
            const newEvents = {
                events: response.result,
                page: filterObject.page.page

            };
            setEvents(newEvents);
            setPageCount(response.page.totalSize / response.page.size);
        }, 'POST', filterObject);
    };

    const onDelete = (id) => {
        state.fetchWithToken(EVENTS_URL + '/delete/' + id, () =>
            setEvents(events.events.filter(apartment => apartment.id !== id)), 'DELETE')
    };

    const onChangeSearchObject = (field, value) => {
        const object = {...searchObject};
        if (!isEqual(get(object, field), value)) {
            set(object, field, value);
            setSearchObject(object);
        }
    };

    const onClearSearchObject = () => setSearchObject({...SEARCH_OBJECT_DEFAULT});

    return <>
        <Container className='search-container'>
            <SearchForm setEvents={setEvents}
                        filterObject={filterObject}
                        setFilterObject={setFilterObject}
                        setPageCount={setPageCount}
                        searchObject={searchObject}
                        onChangeSearchObject={onChangeSearchObject}
                        onClearSearchObject={onClearSearchObject}
            />
        </Container>
        <Container className='events-container'>
            <GroupItems
                onDelete={onDelete}
                onClick={onClickChooseEvent}
                items={events.events}
                isEvents
            />
        </Container>
        <br/>
        {pageCount <= 1 || <Container sm="12" md={{size: 6, offset: 3}}>
            <div className="react-paginate">
                <ReactPaginate
                    previousLabel={"← Previous"}
                    nextLabel={"Next →"}
                    breakLabel={'...'}
                    breakClassName={'break-me'}
                    pageCount={pageCount}
                    marginPagesDisplayed={2}
                    pageRangeDisplayed={5}
                    onPageChange={onClickNextPage}
                    containerClassName={'pagination'}
                    subContainerClassName={'pages pagination'}
                    activeClassName={'active'}
                />
            </div>
        </Container>
        }
    </>;
};

export default Events;