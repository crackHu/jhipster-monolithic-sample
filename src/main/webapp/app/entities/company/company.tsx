import * as React from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import { Button, InputGroup } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import {
  Translate,
  translate,
  ICrudSearchAction,
  ICrudGetAllAction,
  getSortState,
  IPaginationBaseState,
  getPaginationItemsNumber,
  JhiPagination
} from 'react-jhipster';
import { FaPlus, FaEye, FaPencil, FaTrash, FaSort, FaSearch } from 'react-icons/lib/fa';

import { getSearchEntities, getEntities } from './company.reducer';
import { ICompany } from 'app/shared/model/company.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface ICompanyProps {
  getEntities: ICrudGetAllAction<ICompany>;
  getSearchEntities: ICrudSearchAction<ICompany>;
  companyList: ICompany[];
  totalItems: 0;
  location: any;
  history: any;
  match: any;
}

export interface ICompanyState extends IPaginationBaseState {
  search: string;
}

export class Company extends React.Component<ICompanyProps, ICompanyState> {
  state: ICompanyState = {
    search: '',
    ...getSortState(this.props.location, ITEMS_PER_PAGE)
  };

  componentDidMount() {
    this.getEntities();
  }

  search = () => {
    if (this.state.search) {
      this.props.getSearchEntities(this.state.search);
    }
  };

  clear = () => {
    this.props.getEntities();
    this.setState({
      search: ''
    });
  };

  handleSearch = event => this.setState({ search: event.target.value });

  sort = prop => () => {
    this.setState(
      {
        order: this.state.order === 'asc' ? 'desc' : 'asc',
        sort: prop
      },
      () => this.sortEntities()
    );
  };

  sortEntities() {
    this.getEntities();
    this.props.history.push(`${this.props.location.pathname}?page=${this.state.activePage}&sort=${this.state.sort},${this.state.order}`);
  }

  handlePagination = activePage => this.setState({ activePage }, () => this.sortEntities());

  getEntities = () => {
    const { activePage, itemsPerPage, sort, order } = this.state;
    this.props.getEntities(activePage - 1, itemsPerPage, `${sort},${order}`);
  };

  render() {
    const { companyList, match, totalItems } = this.props;
    return (
      <div>
        <h2 id="page-heading">
          <Translate contentKey="jhipsterMonolithicSampleApp.company.home.title">Companies</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FaPlus />
            <Translate contentKey="jhipsterMonolithicSampleApp.company.home.createLabel">Create new Company</Translate>
          </Link>
        </h2>
        <div className="row">
          <div className="col-sm-12">
            <AvForm onSubmit={this.search}>
              <AvGroup>
                <InputGroup>
                  <AvInput
                    type="text"
                    name="search"
                    value={this.state.search}
                    onChange={this.handleSearch}
                    placeholder={translate('jhipsterMonolithicSampleApp.company.home.search')}
                  />
                  <Button className="input-group-addon">
                    <FaSearch />
                  </Button>
                  <Button type="reset" className="input-group-addon" onClick={this.clear}>
                    <FaTrash />
                  </Button>
                </InputGroup>
              </AvGroup>
            </AvForm>
          </div>
        </div>
        <div className="table-responsive">
          <table className="table table-striped">
            <thead>
              <tr>
                <th className="hand" onClick={this.sort('id')}>
                  <Translate contentKey="global.field.id">ID</Translate> <FaSort />
                </th>
                <th className="hand" onClick={this.sort('name')}>
                  <Translate contentKey="jhipsterMonolithicSampleApp.company.name">Name</Translate> <FaSort />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {companyList.map((company, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${company.id}`} color="link" size="sm">
                      {company.id}
                    </Button>
                  </td>
                  <td>{company.name}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${company.id}`} color="info" size="sm">
                        <FaEye />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${company.id}/edit`} color="primary" size="sm">
                        <FaPencil />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${company.id}/delete`} color="danger" size="sm">
                        <FaTrash />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        <div className="row justify-content-center">
          <JhiPagination
            items={getPaginationItemsNumber(totalItems, this.state.itemsPerPage)}
            activePage={this.state.activePage}
            onSelect={this.handlePagination}
            maxButtons={5}
          />
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ company }) => ({
  companyList: company.entities,
  totalItems: company.totalItems
});

const mapDispatchToProps = {
  getSearchEntities,
  getEntities
};

export default connect(mapStateToProps, mapDispatchToProps)(Company);
