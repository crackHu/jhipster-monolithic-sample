import * as React from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import { Button } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FaArrowLeft, FaPencil } from 'react-icons/lib/fa';

import { getEntity } from './customer.reducer';
import { ICustomer } from 'app/shared/model/customer.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICustomerDetailProps {
  getEntity: ICrudGetAction<ICustomer>;
  customer: ICustomer;
  match: any;
}

export class CustomerDetail extends React.Component<ICustomerDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { customer } = this.props;
    return (
      <div className="row justify-content-center">
        <div className="col-8">
          <h2>
            <Translate contentKey="jhipsterMonolithicSampleApp.customer.detail.title">Customer</Translate> [<b>{customer.id}</b>]
          </h2>
          <dl className="row-md jh-entity-details">
            <dt>
              <span id="shardingId">
                <Translate contentKey="jhipsterMonolithicSampleApp.customer.shardingId">Sharding Id</Translate>
              </span>
            </dt>
            <dd>{customer.shardingId}</dd>
          </dl>
          <Button tag={Link} to="/entity/customer" replace color="info">
            <FaArrowLeft />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          <Button tag={Link} to={`/entity/customer/${customer.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ customer }) => ({
  customer: customer.entity
});

const mapDispatchToProps = { getEntity };

export default connect(mapStateToProps, mapDispatchToProps)(CustomerDetail);
