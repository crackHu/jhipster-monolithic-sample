import * as React from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import { Button } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FaArrowLeft, FaPencil } from 'react-icons/lib/fa';

import { getEntity } from './employee.reducer';
import { IEmployee } from 'app/shared/model/employee.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IEmployeeDetailProps {
  getEntity: ICrudGetAction<IEmployee>;
  employee: IEmployee;
  match: any;
}

export class EmployeeDetail extends React.Component<IEmployeeDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { employee } = this.props;
    return (
      <div className="row justify-content-center">
        <div className="col-8">
          <h2>
            <Translate contentKey="jhipsterMonolithicSampleApp.employee.detail.title">Employee</Translate> [<b>{employee.id}</b>]
          </h2>
          <dl className="row-md jh-entity-details">
            <dt>
              <span id="name">
                <Translate contentKey="jhipsterMonolithicSampleApp.employee.name">Name</Translate>
              </span>
            </dt>
            <dd>{employee.name}</dd>
            <dt>
              <span id="shardingId">
                <Translate contentKey="jhipsterMonolithicSampleApp.employee.shardingId">Sharding Id</Translate>
              </span>
            </dt>
            <dd>{employee.shardingId}</dd>
          </dl>
          <Button tag={Link} to="/entity/employee" replace color="info">
            <FaArrowLeft />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          <Button tag={Link} to={`/entity/employee/${employee.id}/edit`} replace color="primary">
            <FaPencil />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ employee }) => ({
  employee: employee.entity
});

const mapDispatchToProps = { getEntity };

export default connect(mapStateToProps, mapDispatchToProps)(EmployeeDetail);
