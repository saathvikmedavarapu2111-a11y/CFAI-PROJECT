let currentArray = [];
let isAnimating = false;
let animationTimeouts = [];

window.addEventListener('DOMContentLoaded', () => {
    const warning = document.getElementById('data-warning');
    const visualizeBtn = document.getElementById('btn-visualize');
    const arrayDisplay = document.getElementById('loaded-array-display');
    
    if (window.sortingDataLoaded && typeof sortingData !== 'undefined') {
        warning.style.display = 'none';
        visualizeBtn.disabled = false;
        currentArray = sortingData.originalArray;
        arrayDisplay.textContent = currentArray.join(', ');
        resetVisualization();
    } else {
        warning.style.display = 'block';
        visualizeBtn.disabled = true;
        arrayDisplay.textContent = 'Awaiting data... Please run SortComparison.java first.';
    }
});

function resetVisualization() {
    animationTimeouts.forEach(clearTimeout);
    animationTimeouts = [];
    
    renderBars('selection', currentArray);
    renderBars('quick', currentArray);
    
    document.getElementById('selection-status').textContent = 'Ready to visualize';
    document.getElementById('quick-status').textContent = 'Ready to visualize';
    
    ['metric-selection-time', 'metric-selection-compares', 'metric-selection-swaps',
     'metric-quick-time', 'metric-quick-compares', 'metric-quick-swaps'].forEach(id => {
        document.getElementById(id).textContent = '-';
    });
    
    document.getElementById('btn-visualize').disabled = false;
    document.getElementById('btn-reset').disabled = true;
    isAnimating = false;
}

function renderBars(type, array, active = {}) {
    const container = document.getElementById(`${type}-bars`);
    container.innerHTML = '';
    const maxVal = Math.max(...array, 1);
    
    array.forEach((val, idx) => {
        const bar = document.createElement('div');
        bar.className = 'bar';
        bar.style.height = `${(val / maxVal) * 85 + 10}%`;
        
        const label = document.createElement('span');
        label.className = 'bar-value';
        label.textContent = val;
        bar.appendChild(label);
        
        if (active.comparing && active.comparing.includes(idx)) bar.classList.add('compare');
        else if (active.swapping && active.swapping.includes(idx)) bar.classList.add('swap');
        else if (active.minIdx === idx) bar.classList.add('sel-min');
        else if (active.pivotIdx === idx) bar.classList.add('quick-pivot');
        else if (active.isSorted) bar.classList.add('sorted');
        
        container.appendChild(bar);
    });
}

function startVisualization() {
    if (isAnimating || typeof sortingData === 'undefined') return;
    isAnimating = true;
    
    document.getElementById('btn-visualize').disabled = true;
    document.getElementById('btn-reset').disabled = false;
    
    const fmt = ns => ns < 1000000 ? `${(ns / 1000).toFixed(1)} µs` : `${(ns / 1000000).toFixed(2)} ms`;
    
    document.getElementById('metric-selection-time').textContent = fmt(sortingData.selection.timeNs);
    document.getElementById('metric-selection-compares').textContent = sortingData.selection.comparisons;
    document.getElementById('metric-selection-swaps').textContent = sortingData.selection.swaps;
    
    document.getElementById('metric-quick-time').textContent = fmt(sortingData.quick.timeNs);
    document.getElementById('metric-quick-compares').textContent = sortingData.quick.comparisons;
    document.getElementById('metric-quick-swaps').textContent = sortingData.quick.swaps;
    
    const selSteps = sortingData.selection.steps;
    const qSteps = sortingData.quick.steps;
    const maxSteps = Math.max(selSteps.length, qSteps.length);
    let step = 0;
    
    function play() {
        if (step >= maxSteps) {
            const sorted = currentArray.slice().sort((a,b)=>a-b);
            renderBars('selection', sorted, { isSorted: true });
            renderBars('quick', sorted, { isSorted: true });
            document.getElementById('selection-status').textContent = '✅ Selection Sort completed!';
            document.getElementById('quick-status').textContent = '✅ Quick Sort completed!';
            isAnimating = false;
            document.getElementById('btn-visualize').disabled = false;
            return;
        }
        
        if (step < selSteps.length) {
            const s = selSteps[step];
            renderBars('selection', s.array, {
                comparing: s.type === 'compare' ? [s.index1, s.index2] : null,
                swapping: s.type === 'swap' ? [s.index1, s.index2] : null,
                minIdx: s.type === 'min' ? s.index1 : null
            });
            document.getElementById('selection-status').innerHTML = s.type === 'compare' ? 
                `🔍 Comparing elements at index <b>${s.index1}</b> and <b>${s.index2}</b>` :
                s.type === 'swap' ? `🔄 Swapping index <b>${s.index1}</b> with index <b>${s.index2}</b>` : 
                `📌 Set current minimum index to <b>${s.index1}</b>`;
        }
        
        if (step < qSteps.length) {
            const q = qSteps[step];
            renderBars('quick', q.array, {
                comparing: q.type === 'compare' ? [q.index1, q.index2] : null,
                swapping: q.type === 'swap' ? [q.index1, q.index2] : null,
                pivotIdx: q.type === 'pivot' ? q.index1 : null
            });
            document.getElementById('quick-status').innerHTML = q.type === 'compare' ? 
                `🔍 Comparing element at index <b>${q.index1}</b> with pivot` :
                q.type === 'swap' ? `🔄 Swapping index <b>${q.index1}</b> and index <b>${q.index2}</b>` : 
                `🎯 Selected pivot element at index <b>${q.index1}</b>`;
        }
        
        step++;
        animationTimeouts.push(setTimeout(play, 400));
    }
    play();
}
