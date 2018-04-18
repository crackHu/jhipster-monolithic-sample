import * as React from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import { Button } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FaArrowLeft, FaPencil } from 'react-icons/lib/fa';

import { getEntity } from './goods.reducer';
import { IGoods } from 'app/shared/model/goods.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IGoodsDetailProps {
  getEntity: ICrudGetAction<IGoods>;
  goods: IGoods;
  match: any;
}

export class GoodsDetail extends React.Component<IGoodsDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { goods } = this.props;
    return (
      <div className="row justify-content-center">
        <div className="col-8">
          <h2>
            <Translate contentKey="jhipsterMonolithicSampleApp.goods.detail.title">Goods</Translate> [<b>{goods.id}</b>]
          </h2>
          <dl className="row-md jh-entity-details">
            <dt>
              <span id="name">
                <Translate contentKey="jhipsterMonolithicSampleApp.goods.name">Name</Translate>
              </span>
            </dt>
            <dd>{goods.name}</dd>
          </dl>
          <Button tag={Link} to="/entity/goods" replace color="info">
            <FaArrowLeft />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          <Button tag={Link} to={`/entity/goods/${goods.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ goods }) => ({
  goods: goods.entity
});

const mapDispatchToProps = { getEntity };

export default connect(mapStateToProps, mapDispatchToProps)(GoodsDetail);
