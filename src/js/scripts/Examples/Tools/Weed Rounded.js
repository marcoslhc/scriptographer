////////////////////////////////////////////////////////////////////////////////
// Values

var values = {
	size: 10,
	minAmount: 5,
	maxAmount: 15,
	minWidth: 0.1,
	maxWidth: 5
};

////////////////////////////////////////////////////////////////////////////////
// Mouse handling

tool.eventInterval = 1000 / 100; // 100 times a second

var branches;

function onMouseDown(event) {
	branches = [];
	var count = Math.rand(values.minAmount, values.maxAmount);
	var group = new Group();
	for (var i = 0; i < count; i++)
		branches.push(new Branch(event.point, group));
}

function onMouseUp(event) {
	for (var i in branches)
		branches[i].finish();
}

function onMouseDrag(event) {
	for (var i in branches)
		branches[i].grow();
}

// Branch:
function Branch(point, group) {
	this.point = point;
	this.vector = new Point(1, 0).rotate(Math.random() * 360);
	this.path = new Path();
	this.path.add(point);
	this.path.strokeWidth = values.minWidth + Math.random()
			* (values.maxWidth - values.minWidth);
	group.appendTop(this.path);
	this.rotate = 12;
	this.count = 0;
	this.max = 0;
}

Branch.prototype.grow = function() {
	if (this.count++ < this.max) {
		this.vector = this.vector.rotate(this.rotate);
	} else {
		this.vector.length = (1 - Math.random() * 0.5) * values.size;
		this.max = Math.round(Math.random() * 360 / Math.abs(this.rotate));
		this.rotate *= -1;
		this.count = 0;
	}
	this.point += this.vector;
	this.path.add(this.point);
};

Branch.prototype.finish = function() {
	this.path.pointsToCurves();
};

////////////////////////////////////////////////////////////////////////////////
// Interface

var components = {
	size: {
		label: 'Radius',
		steppers: true
	},
	minAmount: {
		label: 'Minimal Amount',
		min: 0,
		steppers: true
	},
	maxAmount: {
		label: 'Maximal Amount',
		min: 0,
		steppers: true
	},
	minWidth: {
		label: 'Minimal Stroke Width',
		min: 0,
		steppers: true
	},
	maxWidth: {
		label: 'Maximal Stroke Width',
		min: 0,
		steppers: true
	}
};

var palette = new Palette('Weed Rounded', components, values);
