import {useEffect, useState} from 'react';

export const usePosition = () => {
    const [position, setPosition] = useState({lat: 18.5204, lng: 73.8567});
    const [error, setError] = useState(null);

    const onChange = ({coords}) => {
        setPosition({
            lat: coords.latitude,
            lng: coords.longitude,
        });
    };
    const onError = (error) => {
        setError(error.message);
    };
    useEffect(() => {
        const geo = navigator.geolocation;
        if (!geo) {
            setError('Geolocation is not supported');
            return;
        }
        const watcher = geo.watchPosition(onChange, onError);
        return () => geo.clearWatch(watcher);
    }, []);
    return {...position, error};
}