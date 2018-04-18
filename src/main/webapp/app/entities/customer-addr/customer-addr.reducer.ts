import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';
import { SERVER_API_URL } from 'app/config/constants';

import { ICustomerAddr } from 'app/shared/model/customer-addr.model';

export const ACTION_TYPES = {
  SEARCH_CUSTOMERADDRS: 'customerAddr/SEARCH_CUSTOMERADDRS',
  FETCH_CUSTOMERADDR_LIST: 'customerAddr/FETCH_CUSTOMERADDR_LIST',
  FETCH_CUSTOMERADDR: 'customerAddr/FETCH_CUSTOMERADDR',
  CREATE_CUSTOMERADDR: 'customerAddr/CREATE_CUSTOMERADDR',
  UPDATE_CUSTOMERADDR: 'customerAddr/UPDATE_CUSTOMERADDR',
  DELETE_CUSTOMERADDR: 'customerAddr/DELETE_CUSTOMERADDR',
  RESET: 'customerAddr/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: {},
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

// Reducer

export default (state = initialState, action) => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_CUSTOMERADDRS):
    case REQUEST(ACTION_TYPES.FETCH_CUSTOMERADDR_LIST):
    case REQUEST(ACTION_TYPES.FETCH_CUSTOMERADDR):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_CUSTOMERADDR):
    case REQUEST(ACTION_TYPES.UPDATE_CUSTOMERADDR):
    case REQUEST(ACTION_TYPES.DELETE_CUSTOMERADDR):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_CUSTOMERADDRS):
    case FAILURE(ACTION_TYPES.FETCH_CUSTOMERADDR_LIST):
    case FAILURE(ACTION_TYPES.FETCH_CUSTOMERADDR):
    case FAILURE(ACTION_TYPES.CREATE_CUSTOMERADDR):
    case FAILURE(ACTION_TYPES.UPDATE_CUSTOMERADDR):
    case FAILURE(ACTION_TYPES.DELETE_CUSTOMERADDR):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_CUSTOMERADDRS):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_CUSTOMERADDR_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_CUSTOMERADDR):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_CUSTOMERADDR):
    case SUCCESS(ACTION_TYPES.UPDATE_CUSTOMERADDR):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_CUSTOMERADDR):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = SERVER_API_URL + '/api/customer-addrs';
const apiSearchUrl = SERVER_API_URL + '/api/_search/customer-addrs';

// Actions

export const getSearchEntities: ICrudSearchAction<ICustomerAddr> = query => ({
  type: ACTION_TYPES.SEARCH_CUSTOMERADDRS,
  payload: axios.get(`${apiSearchUrl}?query=` + query) as Promise<ICustomerAddr>
});

export const getEntities: ICrudGetAllAction<ICustomerAddr> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_CUSTOMERADDR_LIST,
    payload: axios.get(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`) as Promise<ICustomerAddr>
  };
};

export const getEntity: ICrudGetAction<ICustomerAddr> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_CUSTOMERADDR,
    payload: axios.get(requestUrl) as Promise<ICustomerAddr>
  };
};

export const createEntity: ICrudPutAction<ICustomerAddr> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_CUSTOMERADDR,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ICustomerAddr> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_CUSTOMERADDR,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICustomerAddr> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_CUSTOMERADDR,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
